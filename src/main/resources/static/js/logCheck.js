const rowBuilder = (...col) => {
    let row = "<tr>";
    for (let i=0;i<col.length;i++) {
        row = row + "<td>"+col[0]+"</td>";
    }
    row = row + "</tr>";
    return row;
}

function initialize() {

}

function updateTable() {
    let getTableList = new XMLHttpRequest();
    getTableList.open("GET", "/get/statusList")
}