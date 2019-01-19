const domainName = "https://fandoco-wallet.herokuapp.com";

//onclick login button
function displayLogin() {
    sessionStorage.setItem("source", "getTypes");

    if (localStorage.getItem("token") == null) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;
    } else {
        getTypes();
        document.getElementById("nav").innerHTML = document.getElementById("sidemenuArea").innerHTML;

    }
}

function displayLogout() {
    document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;
    document.getElementById("nav").innerHTML = "";
    localStorage.removeItem("token") //Delete accesstoken
}

//onclick login button submitting id password
function checkLogin() {
    let token;
    let id = document.getElementById("id").value;
    let password = document.getElementById("password").value;

    let url = domainName + "/login";
    let body = "{\"userName\" : \"" + id + "\",\"password\" : \"" + password + "\"}";

    let postreq = new XMLHttpRequest();
    postreq.open('POST', url, true);
    postreq.setRequestHeader('Content-type', 'application/json');
    postreq.send(body);

    postreq.onreadystatechange = function () {//Call a function when the state changes.
        if (postreq.readyState === 4 && postreq.status === 200) {
            let myHeader = postreq.getResponseHeader("Authorization");

            localStorage.setItem('token', myHeader); // write
            //token = localStorage.getItem('token'); // read
            document.getElementById("details").innerHTML = "";
            switch (sessionStorage.getItem("source")
                ) {
                case "getTypes":
                    getTypes();
                    break;
                case "addData":
                    addData();
                    break;
                default:
                    getTypes();
                    break;
            }
            document.getElementById("nav").innerHTML = document.getElementById("sidemenuArea").innerHTML;

        } else {
            document.getElementById("info").innerHTML = "ID or Password is not valid";
            document.getElementById("id").value = "";
            document.getElementById("password").value = "";
        }
    }

}

function populateTypesDropdown(id) {
    let url2 = domainName + "/types";

    //Fetch the content of the url using the XMLHttpRequest object
    let req2 = new XMLHttpRequest();
    req2.open("GET", url2);
    req2.setRequestHeader("Authorization", localStorage.getItem('token'));
    req2.send(null);

    //register an event handler function
    req2.onreadystatechange = function () {
        if (req2.readyState === 4 && req2.status === 200) {
            let response = req2.responseText;
            const types = JSON.parse(response);

            for (let i = 0; i < types.length; i++) {
                createSelectDropdown(types[i], id)

            }
        }
    }
}

function getTypes() {
    sessionStorage.setItem("source", "getTypes");
    if (localStorage.getItem('token') == null) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    }
    document.getElementById("details").innerHTML = document.getElementById("typesDropdown").innerHTML;

    populateTypesDropdown("categoryDropDown");
}

function createSelectDropdown(itemName, id) {
    let dropdownOptions;
    let item;

    dropdownOptions = document.createElement("option");
    dropdownOptions.setAttribute("value", itemName);
    item = document.createTextNode(itemName);
    dropdownOptions.appendChild(item);
    document.getElementById(id).appendChild(dropdownOptions);
}

function getDatabyType(type, page, callback) {
    if (localStorage.getItem('token') == null) {
        document.getElementById("details").innerHTML = document.getElementById("loginform").innerHTML;

        return;
    }
    let url1 = domainName + "/data?type=" + type;

    //Fetch the content of the url using the XMLHttpRequest object
    let req1 = new XMLHttpRequest();
    req1.open("GET", url1);
    req1.setRequestHeader("Authorization", localStorage.getItem('token'));

    req1.send(null);

    //register an event handler function
    req1.onreadystatechange = function () {
        if (req1.readyState === 4 && req1.status === 200) {
            let response = req1.responseText;
            let listOfSecureDetails = JSON.parse(response);

            if (page === "getData") {
                for (let i = 0; i < listOfSecureDetails.length; i++) {

                    let key = listOfSecureDetails[i].key;
                    let value = listOfSecureDetails[i].value;

                    addToDataTable(key, value);
                }
            } else {
                callback(listOfSecureDetails);
            }
        }
    }
}

function addToDataTable(key, value) {

    const table = document.getElementById("dataTable");

    const row = table.insertRow();
    const cell1 = row.insertCell();
    const cell2 = row.insertCell();
    cell1.innerHTML = key;
    cell2.innerHTML = value;
}

function showDetails() {
    let category = document.getElementById("categoryDropDown");
    let selectedOption = category.options[category.selectedIndex].value;

    let Table = document.getElementById("dataTable");
    Table.innerHTML = "";
    Table = document.getElementById("dataTable");

    const header = Table.createTHead();
    header.innerHTML = selectedOption;

    getDatabyType(selectedOption, "getData");
}

function help() {
    document.getElementById("details").innerHTML = "<p>HELP</p>"
}

