import axios from "axios";

export function signupApi(email, password) {
    return axios.post('/api/auth/signup', {
        email: email,
        password: password
    })
        .then(res => {
            return "회원가입에 성공했습니다.";
        })
        .catch(error => {
            const errorMsg = error.response.data.message;

            return '회원가입에 실패했습니다.\n' + errorMsg;
        });
}
