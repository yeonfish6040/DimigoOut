let timetable;
let lastTime;
let currentStatus = 0;
let currentSubject = "";
let lastTyping = new Date().getTime();
let lastText = document.getElementById("alim").value;
let saveMsg = "DimigoOut알림저장됨".split("");

let timeSequence = {};


const timeFormat2sec = (tf) => {
    return (Math.floor(tf/100)*3600+(tf%100)*60);
}

async function initialize() {
    timeSequence = await (await fetch("/public/schedule")).json()
    $(".input").hide()

    updateStatusList()

    setInterval(alimSaver, 100);
    setInterval(updateCurrentTime, 100);

    window.addEventListener("message", messageHandler);
    document.addEventListener("fullscreenchange", checkFullscreen);
    document.getElementById("alim").addEventListener("keydown", lastTypingUpdater);
    document.getElementById("full_screen-btn").addEventListener("click", toggleFullscreen);
    document.getElementById("seat_btn").addEventListener("click", () => location.href = "/seat_change");
    document.getElementById("checkLog-btn").addEventListener("click", e => window.location = "/check_log");
    Array.prototype.forEach.call(document.getElementsByClassName("input"), (e) => e.addEventListener("keydown", reason_enter));
    Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => {e.addEventListener("click", selector_click)})
    Array.prototype.forEach.call(document.getElementsByClassName("input_reason"), (e) => e.querySelector("input").addEventListener("focusout", selector_focusout));
}

function toggleFullscreen(e) {
    if (!document.fullscreenElement) {
        navigator.mediaDevices.getUserMedia({ audio: true }).then(() => {
            document.getElementById("full_screen-btn").requestFullscreen();
        }).catch(() => {})
    } else {
        document.exitFullscreen();
    }
}

function checkFullscreen(e) {
    if (document.fullscreenElement) {
        document.getElementById("full_screen-btn").classList.add("fullscreen");
    } else {
        document.getElementById("full_screen-btn").classList.remove("fullscreen");
    }
}

function updateCurrentTime() {
    // let currentTime = 1430;
    let currentTime = new Date().getHours()*100+new Date().getMinutes();
    let current = timeSequence.filter(e => parseInt(e.from) <= currentTime && parseInt(e.to) > currentTime);
    if (current.length === 0)
        current = { name: "쉬는시간", id: "rest", to: currentTime+(10-currentTime%10), from: currentTime+(10-currentTime%10)-10 };
    else
        current = current[0];

    if (lastTime === current) {
        let timeLeft = timeFormat2sec(parseInt(current.to))-(timeFormat2sec(currentTime)+new Date().getSeconds());
        let timeLeftPer = (timeLeft/(timeFormat2sec(parseInt(current.to))-timeFormat2sec(parseInt(current.from)))) * 100;
        timeLeft = { current: current, subject: currentSubject, hours: (!(Math.floor(timeLeft / 60 / 60) < 1) ? Math.floor(timeLeft / 60 / 60) : 0), minutes: Math.floor(timeLeft / 60) % 60, seconds: (timeLeft % 60 ? timeLeft % 60 : 0) }
        let timeLeft_str = (timeLeft.hours > 0 ? timeLeft.hours+ "시간 " : "") + ( timeLeft.hours > 0 || timeLeft.minutes > 0 ? timeLeft.minutes + "분 ": "") + timeLeft.seconds+"초";

        // 자습시간 30분 제한
        if (current.id.endsWith("_study-1") || current.id.endsWith("_study-2")) {
            if (((new Date().getHours()*3600+new Date().getMinutes()*60+new Date().getSeconds()) - timeFormat2sec(current.from)) < (600*3))
                currentSubject = " (이동 불가)"
            else
                currentSubject = " (이동 가능)"
        }

        if (document.fullscreenElement) {
            // 심자 안내방송
            if (parseInt(current.to)-5 <= currentTime && parseInt(current.to) > currentTime) {
                if (current.id === "hakbonggwan_study1-2" && currentStatus !== 1) {
                    try {
                        new Audio("/sounds/announce_middle.mp3").play();
                    } catch (e) {}

                    currentStatus = 1;
                }else if (current.id === "hakbonggwan_study2" && currentStatus !== 2) {
                    try {
                        new Audio("/sounds/announce_last.mp3").play();
                    } catch (e) {}

                    currentStatus = 2;
                }
            }
            // console.log(JSON.stringify(timeLeft))
            document.getElementById("notth3dev_timer").contentWindow.postMessage(JSON.stringify(timeLeft))
        }else {
            document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .progress_bar > .inner_progress_bar").style.width = (100-timeLeftPer)+"%";
            document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .progress_bar > .timeLeft").innerText = timeLeft_str+" 남음";
        }
    } else {
        lastTime = current;
        if (lastTime && lastTime.id.includes("perio")) {
            let perio = lastTime.id.replace("perio_", "");

            let getSubject = new XMLHttpRequest();
            getSubject.open("GET", "/get/timetable");
            getSubject.onload = (e) => {
                let result = JSON.parse(e.target.responseText)["hisTimetable"][1]["row"];
                currentSubject = " ("+result.filter(s => s.PERIO == perio)[0]["ITRT_CNTNT"]+")";
                console.log(currentSubject);
            }
            getSubject.send();
        }else currentSubject = "";

    }
    let timeline = "";
    document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .current").innerText = current.name+currentSubject+timeline;
}

function alimSaver() {
    if (new Date().getTime()-lastTyping > 1000 && document.getElementById("alim").value != lastText) {
        lastText = document.getElementById("alim").value;

        let save = new XMLHttpRequest();
        save.open("POST", "/set/alim");
        save.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        save.onload = (e) => {
            if (e.target.status === 200) {
                let i = 0;
                let saveMsgInterval = setInterval(() => {
                    if (i > 13) return clearInterval(saveMsgInterval);
                    saveMsg.push(saveMsg.splice(0, 1));
                    document.title = saveMsg.join("").substring(0, 9);
                    i++;
                }, 200);
            }else {
                if (e.target.status === 403) alert("권한이 없습니다!");
                console.error(e);
            }
        }
        save.send("text="+lastText);
    }
}

function selector_click(e) {
    e.preventDefault();

    let className    = Array.prototype.filter.call(e.target.classList, e => e.startsWith("type_"))[0];
    let selection = Array.prototype.filter.call(document.getElementsByClassName(className), e => Array.prototype.indexOf.call(e.classList, "status_selection") !== -1);
    let label = Array.prototype.filter.call(document.getElementsByClassName(className), e => Array.prototype.indexOf.call(e.classList, "label") !== -1);
    let input = Array.prototype.filter.call(document.getElementsByClassName(className), e => Array.prototype.indexOf.call(e.classList, "input") !== -1);

    let status = className.indexOf("type_water") !== -1 ? 1 : className.indexOf("type_afterClass") !== -1 ? 2 : className.indexOf("type_club") !== -1 ? 3 : className.indexOf("type_etc") !== -1 ? 4 : 0

    if (selection[0].classList.contains("disabled")) return;

    if (selection[0].classList.contains("active") || status === 1) {
        let register_water = new XMLHttpRequest();
        register_water.open("POST", "/set/status?status="+status);
        register_water.setRequestHeader("content-type", "application/x-www-form-urlencoded")
        register_water.onload = (e) => {
            // if (register_water.responseText === "true") alert("성공");
            // else alert("오류발생");
            if (register_water.responseText === "false") alert("실패")
            return updateStatusList();
        }
        register_water.send();
    } else {
        $(label).slideUp();
        $(input).slideDown();
        $(input).focus();
    }
}

function selector_focusout(e) {
    e.preventDefault();

    let className    = Array.prototype.filter.call(e.target.classList, e => e.startsWith("type_"))[0];
    let label = Array.prototype.filter.call(document.getElementsByClassName(className), e => Array.prototype.indexOf.call(e.classList, "label") !== -1);
    let input = Array.prototype.filter.call(document.getElementsByClassName(className), e => Array.prototype.indexOf.call(e.classList, "input") !== -1);

    input[0].value = "";
    $(label).slideDown();
    $(input).slideUp();
}

function reason_enter(e) {
    if (e.keyCode === 13) {
        e.preventDefault();

        let className    = Array.prototype.filter.call(e.target.classList, e => e.startsWith("type_"))[0];
        let status = className.indexOf("type_water") !== -1 ? 1 : className.indexOf("type_afterClass") !== -1 ? 2 : className.indexOf("type_club") !== -1 ? 3 : className.indexOf("type_etc") !== -1 ? 4 : 0

        let register_status = new XMLHttpRequest();
        register_status.open("POST", e.target.classList.contains("active") ? "/set/status?status="+status : "/set/status?status="+status+"&reason="+e.target.value);
        register_status.setRequestHeader("content-type", "application/x-www-form-urlencoded")
        register_status.onload = (e) => {
            // if (register_water.responseText === "true") alert("성공");
            // else alert("오류발생");
            if (register_status.responseText === "false") alert("실패");

            $("#alim").focus();

            return updateStatusList();
        }
        register_status.send();
    }
}

function lastTypingUpdater(event) {
    lastTyping = new Date().getTime()
}

function updateStatusList() {
    let getCurStatus = new XMLHttpRequest();
    getCurStatus.open("GET", "/get/status")
    getCurStatus.onload = (e) => {
        let status = parseInt(getCurStatus.responseText);
        switch (status) {
            case 0:
                Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => { e.classList.remove("disabled"); e.classList.remove("active"); e.classes = Array.prototype.join.call(e, " ") })
                break;
            case 1:
                Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => { e.classList.add("disabled"); e.classList.remove("active"); if (e.classList.contains("type_water")) { e.classList.remove("disabled"); e.classList.add("active") }})
                break;
            case 2:
                Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => { e.classList.add("disabled"); e.classList.remove("active"); if (e.classList.contains("type_afterClass")) { e.classList.remove("disabled"); e.classList.add("active") }})
                break;
            case 3:
                Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => { e.classList.add("disabled"); e.classList.remove("active"); if (e.classList.contains("type_club")) { e.classList.remove("disabled"); e.classList.add("active") }})
                break;
            case 4:
                Array.prototype.forEach.call(document.getElementsByClassName("status_selection"), (e) => { e.classList.add("disabled"); e.classList.remove("active"); if (e.classList.contains("type_etc")) { e.classList.remove("disabled"); e.classList.add("active") }})
                break;
        }
    }
    getCurStatus.send()
}


function messageHandler(e) {
    if (e.data === "ext_fullscreen") toggleFullscreen();
}