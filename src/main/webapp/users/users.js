window.addEventListener('load', () => {
    loadUsers();
});

/**
 * Create row for single user.
 * @param {{login: string, role: string, birthDate: string}} user
 * @returns {HTMLTableRowElement} row with character data
 */
function createUserRow(user) {
    const tr = document.createElement('tr');

    const login = document.createElement('td');
    const birthDate = document.createElement('td');
    const role = document.createElement('td');

    login.appendChild(document.createTextNode(user.login));
    birthDate.appendChild(document.createTextNode(user.birthDate));
    role.appendChild(document.createTextNode(user.role));

    tr.appendChild(login);
    tr.appendChild(role);
    tr.appendChild(birthDate);

    return tr;
}
/**
 * Fetches users
 */
function loadUsers() {
    const xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('charactersTableBody');

            clearElementChildren(tbody);

            response.users.forEach(user => {
                tbody.appendChild(createUserRow(user));
            })
        }
    };

    xhttp.open("GET", getContextRoot() + '/api/users', true);
    xhttp.send();
}