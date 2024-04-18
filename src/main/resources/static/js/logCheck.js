let scrollInterval;
let lastStatus = "";

const rowBuilder = (...col) => {
    let row = "<tr>";
    for (let i=0;i<col.length;i++) {
        row = row + "<td>"+col[0]+"</td>";
    }
    row = row + "</tr>";
    return row;
}

function initialize() {
    updateTable()
    setInterval(updateTable, 2000)
    scrollInterval = setInterval(scrollTable, 50)
}

function scrollTable() {
    let container = document.getElementsByClassName("log_window")[0];

    container.scrollBy({left: 0, top: 2 , behavior: 'smooth'});

    if (container.scrollTop >= (container.scrollHeight - container.clientHeight)) { // isMax
        clearInterval(scrollInterval)
        container.scrollTo({left: 0, top: 0, behavior: 'smooth'});
        setTimeout(() => {
            scrollInterval = setInterval(scrollTable, 50)
        }, 1000)
    }
}

function updateTable() {
    let getTableList = new XMLHttpRequest();
    getTableList.open("GET", "/get/statusList");
    getTableList.onload = () => {
        let response = getTableList.responseText;
        let res_json = JSON.parse(response);

        if (lastStatus === response) return;
        lastStatus = response;

        let container = document.getElementById("log_container");
        container.innerHTML = "";
        res_json.forEach((e) => {
            let result = "";

            result = result +
                `<tr class="children">` +
                `   <th scope='row'>${e.time}</th>` +
                `   <td>${e.number}</td>` +
                `   <td>${e.name}</td>` +
                `   <td>${e.status === "1" ? "물/화장실" : e.status === "2" ? "방과후" : e.status === "3" ? "동아리" : "기타"}</td>` +
                `   <td>${e.reason === "null" ? "" : e.reason}</td>` +
                `</tr>`;

            container.innerHTML = container.innerHTML + result;
        })
    }
    getTableList.send();
}