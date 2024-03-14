import axios from "axios";

/**
 * Signup API
 *
 * @param email 이메일
 * @param password 비밀번호
 * @returns {Promise<string | string>} 성공 시 '회원가입에 성공했습니다.', 실패 시 '회원가입에 실패했습니다.\n' + 에러메시지
 */
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
