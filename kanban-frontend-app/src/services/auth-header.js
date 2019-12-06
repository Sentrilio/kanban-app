export default function authHeader() {
  let user = JSON.parse(localStorage.getItem('user'));

  if (user && user.accessToken) {
    let authorizationHeader = 'Bearer ' + user.accessToken;
    console.log(authorizationHeader);
    return { Authorization: authorizationHeader };
  } else {
    return {};
  }
}