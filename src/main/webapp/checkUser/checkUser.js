window.addEventListener('load', () => {
    const checkUserBt = document.getElementById('checkUserBt');
    const uploadAvatarBt = document.getElementById('uploadAvatarBt');
    const deleteAvatarBt = document.getElementById('deleteAvatarBt');

    uploadAvatarBt.addEventListener('click', event=>uploadAvatarAction(event));
    checkUserBt.addEventListener('click', event=>loadUser(event));
    deleteAvatarBt.addEventListener('click', event=>deleteAvatarAction(event));
});

/**
 *
 * @returns {string} user's ide from request path
 */
function getUserLogin() {
    return document.getElementById('login').innerHTML;
}
/**
 * Fetches chosen user
 * @param {Event} event dom event
 */
function loadUser(event) {
    event.preventDefault();
    const xhttp = new XMLHttpRequest();
    const login = document.getElementById('userLogin').value;

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            if(Object.entries(response).length > 0){
                document.getElementById('checkUserPanel').style.display = 'block';

                for (const [key, value] of Object.entries(response)) {
                    let input = document.getElementById(key);
                    if (input) {
                        input.innerHTML = value;
                    }
                }
                document.getElementById('avatarView').src = getContextRoot() + '/api/avatars/' + login;

            }
        }
        else {
            document.getElementById('checkUserPanel').style.display = 'none';
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/users/' + login, true);
    xhttp.send();
}

/**
 * Action event handled for uploading character avatar.
 * @param {Event} event dom event
 */
function uploadAvatarAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/avatars/' + getUserLogin(), true);

    let request = new FormData();
    request.append("avatar", document.getElementById('avatar').files[0]);

    xhttp.send(request);
}

/**
 * Action event handled for deleting character avatar.
 * @param {Event} event dom event
 */
function deleteAvatarAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('DELETE', getContextRoot() + '/api/avatars/' + getUserLogin(), true);

    xhttp.send();

}