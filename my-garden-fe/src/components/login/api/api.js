import axios from "axios";

export async function loginApi(email, password) {
    return axios.post('/api/auth/login', {
        email: email,
        password: password
    })
        .then(({data}) => {
            console.log(data);
            alert("로그인에 성공했습니다.");

            // return {
            // accessToken: data.accessToken,
            // refreshToken: data.refreshToken
            // };
        })
        .catch(error => {
            alert("로그인에 실패했습니다.");
            console.log(error);
        });
}
