let PNum;
let CNum;
let RNum;

function initSeat(event) {
    let PNumInput = document.getElementById("PNum");
    let CNumInput = document.getElementById("CNum");
    PNum = PNumInput.value;
    CNum = CNumInput.value;
    RNum = Math.ceil(PNum / CNum);

    console.log(PNum, CNum)

    let num = 1;
    document.getElementsByClassName("seat_list")[0].innerHTML = "";
    for (let i=0;i<CNum;i++) {
        let col = document.createElement("div");
        col.setAttribute("class", "col "+i);
        for (let j=0;j<RNum;j++) {
            let row = document.createElement("div");
            row.setAttribute("class", "row "+j);
            row.innerText = num;
            col.appendChild(row);

            if (num == PNum) break

            num++;
        }
        document.getElementsByClassName("seat_list")[0].appendChild(col);
        if (num == PNum) break
    }
}

function run(event) {
    initSeat();

    let seatList = document.getElementsByClassName("row");

    let time = 2000;
    let decreasingInterval = () => {
        if (time < 870) return done();
        Array.prototype.forEach.call(seatList, (e) => e.innerText = Math.floor(Math.random() * PNum), 0);
        setTimeout(decreasingInterval, (1000-time));
        time = time - 1;
    }

    let done = () => {
        let range = (start, end) => {
            if(start === end) return [start];
            return [start, ...range(start + 1, end)];
        }

        let pList = range(0, PNum-1);
        for (let i=0;i<PNum;i++) {
            let target = Math.floor(Math.random() * pList.length);
            seatList[i].innerText = pList[target]+1;
            pList.splice(target, 1);
        }
    }

    decreasingInterval();
}


document.getElementById("setPNum").addEventListener("click", initSeat);
document.getElementById("runMachine").addEventListener("click", run);
