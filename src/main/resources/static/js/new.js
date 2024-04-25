let timetable;
let lastTime;
let currentStatus = 0;
let currentSubject = "";
let lastTyping = new Date().getTime();
let lastText = document.getElementById("alim").value;
let saveMsg = "DimigoOut알림저장됨".split("");

let timeSequence = [
    {
        id: "wakeup",
        from: "0630",
        to: "0720",
        name: "기상 및 인원점검"
    },
    {
        id: "breakfast",
        from: "0720",
        to: "0815",
        name: "아침식사"
    },
    {
        id: "morningProgram",
        from: "0815",
        to: "0850",
        name: "아침 프로그램"
    },
    {
        id: "prepareForLecture",
        from: "0850",
        to: "0900",
        name: "조회 및 수업준비"
    },
    {
        id: "perio_1",
        from: "0900",
        to: "0950",
        name: "1교시"
    },
    {
        id: "rest",
        from: "0950",
        to: "1000",
        name: "쉬는시간"
    },
    {
        id: "perio_2",
        from: "1000",
        to: "1050",
        name: "2교시"
    },
    {
        id: "rest",
        from: "1050",
        to: "1100",
        name: "쉬는시간"
    },
    {
        id: "perio_3",
        from: "1100",
        to: "1150",
        name: "3교시"
    },
    {
        id: "rest",
        from: "1150",
        to: "1200",
        name: "쉬는시간"
    },
    {
        id: "perio_4",
        from: "1200",
        to: "1250",
        name: "4교시"
    },
    {
        id: "launch",
        from: "1250",
        to: "1350",
        name: "점심시간"
    },
    {
        id: "perio_5",
        from: "1350",
        to: "1440",
        name: "5교시"
    },
    {
        id: "rest",
        from: "1440",
        to: "1450",
        name: "쉬는시간"
    },
    {
        id: "perio_6",
        from: "1450",
        to: "1540",
        name: "6교시"
    },
    {
        id: "rest",
        from: "1540",
        to: "1550",
        name: "쉬는시간"
    },
    {
        id: "perio_7",
        from: "1550",
        to: "1640",
        name: "7교시"
    },
    {
        id: "afterSchoolPrepare",
        from: "1640",
        to: "1710",
        name: "종례, 청소 및 방과후 수업 준비"
    },
    {
        id: "afterSchool_1",
        from: "1710",
        to: "1750",
        name: "방과후 수업 1타임"
    },
    {
        id: "afterSchool_rest",
        from: "1750",
        to: "1755",
        name: "방과후 수업 쉬는 시간"
    },
    {
        id: "afterSchool_2",
        from: "1755",
        to: "1835",
        name: "방과후 수업 2타임"
    },
    {
        id: "dinner",
        from: "1830",
        to: "1950",
        name: "저녁식사"
    },
    {
        id: "selfStudy_1",
        from: "1950",
        to: "2110",
        name: "야자 1타임"
    },
    {
        id: "selfStudy_rest",
        from: "2110",
        to: "2130",
        name: "야자 쉬는시간"
    },
    {
        id: "selfStudy_2",
        from: "2130",
        to: "2250",
        name: "야자 2타임"
    },
    {
        id: "hakbonggwan_prepare",
        from: "2250",
        to: "2350",
        name: "샤워 및 취침준비 | 심야자습 이동"
    },
    {
        id: "hakbonggwan_study1-1",
        from: "2350",
        to: "2450",
        name: "취침 | 심야자습 1타임"
    },
    {
        id: "hakbonggwan_study1-2",
        from: "0000",
        to: "0050",
        name: "취침 | 심야자습 1타임"
    },
    {
        id: "hakbonggwan_study2",
        from: "0050",
        to: "0150",
        name: "취침 | 심야자습 2타임"
    },
    {
        id: "hakbonggwan_sleep",
        from: "0150",
        to: "0630",
        name: "취침"
    }
];

const timeFormat2sec = (tf) => {
    return (Math.floor(tf/100)*3600+(tf%100)*60);
}

function initialize() {
    let timetableReq = new XMLHttpRequest();
    timetableReq.open("GET", "/get/timetable");

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
        document.getElementById("full_screen-btn").requestFullscreen();
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
        timeLeft = { current: current, hours: (!(Math.floor(timeLeft / 60 / 60) < 1) ? Math.floor(timeLeft / 60 / 60) : 0), minutes: Math.floor(timeLeft / 60) % 60, seconds: (timeLeft % 60 ? timeLeft % 60 : 0) }
        let timeLeft_str = (timeLeft.hours > 0 ? timeLeft.hours+ "시간 " : "") + ( timeLeft.hours > 0 || timeLeft.minutes > 0 ? timeLeft.minutes + "분 ": "") + timeLeft.seconds+"초";
        if (document.fullscreenElement) {
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