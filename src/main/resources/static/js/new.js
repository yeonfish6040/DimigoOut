let timetable;
let lastTime;
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
        to: "0810",
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
        id: "perio_2",
        from: "1000",
        to: "1050",
        name: "2교시"
    },
    {
        id: "perio_3",
        from: "1100",
        to: "1150",
        name: "3교시"
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
        id: "perio_6",
        from: "1450",
        to: "1540",
        name: "6교시"
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
        id: "hakbonggwan",
        from: "2250",
        to: "0630",
        name: "생활관 생활 ㅎ"
    }
];

const timeFormat2sec = (tf) => {
    return (Math.floor(tf/100)*3600+(tf%100)*60)
}

function initialize() {
    let timetableReq = new XMLHttpRequest();
    timetableReq.open("GET", "/get/timetable");

    setInterval(alimSaver, 100);
    setInterval(updateCurrentTime, 100);

    document.getElementById("alim").addEventListener("keydown", lastTypingUpdater);
    document.getElementById("full_screen-btn").addEventListener("click", requestFullscreen)
    document.getElementById("seat_btn").addEventListener("click", () => location.href = "/seat_change");
}

function requestFullscreen() {
    if (!document.fullscreenElement) {
        document.getElementById("full_screen-btn").requestFullscreen();
        document.getElementById("full_screen-btn").classList.add("fullscreen");
    } else {
        document.exitFullscreen();
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
        timeLeft = Math.floor(timeLeft / 60) + "분 " + (timeLeft % 60 ? timeLeft % 60 : '00')+"초"
        document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .progress_bar > .inner_progress_bar").style.width = (100-timeLeftPer)+"%";
        document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .progress_bar > .timeLeft").innerText = timeLeft+" 남음";
    } else {
        lastTime = current;
        if (lastTime && lastTime.id.includes("perio")) {
            let perio = lastTime.id.replace("perio_", "");

            let getSubject = new XMLHttpRequest();
            getSubject.open("GET", "/get/timetable")
            getSubject.onload = (e) => {
                let result = JSON.parse(e.target.responseText)["hisTimetable"][1]["row"];
                let currentSubject = result.filter(s => s.PERIO == perio)[0]["ITRT_CNTNT"];
                console.log(currentSubject)
            }
            getSubject.send()
        }

        document.querySelector(".menu > .menu_element.menu_element-container._2 > .menu_element.menu_element-container._1 > .time_show > .grouper > .current").innerText = current.name;
    }
}

function alimSaver() {
    if (new Date().getTime()-lastTyping > 1000 && document.getElementById("alim").value != lastText) {
        lastText = document.getElementById("alim").value

        let save = new XMLHttpRequest();
        save.open("POST", "/alim/save")
        save.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        save.onload = (e) => {
            if (e.target.status === 200) {
                let i = 0;
                let saveMsgInterval = setInterval(() => {
                    if (i > 13) return clearInterval(saveMsgInterval);
                    saveMsg.push(saveMsg.splice(0, 1))
                    document.title = saveMsg.join("").substring(0, 9)
                    i++;
                }, 200)
            }else {
                if (e.target.status === 403) alert("권한이 없습니다!")
                console.error(e)
            }
        }
        save.send("text="+lastText)
    }
}
function lastTypingUpdater(event) {
    lastTyping = new Date().getTime()
}
