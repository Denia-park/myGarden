import axios from "axios";
import {store} from "@/scripts/store.js";

export function loginApi(email, password) {
    return axios.post('/api/auth/login', {
        email: email,
        password: password
    })
        .then(res => {
            const data = res.data;
            const id = data.data;

            store.commit('setAccount', id);
            alert("로그인에 성공했습니다.");

            sessionStorage.setItem('id', id);

            return id;

            // return {
            // accessToken: data.accessToken,
            // refreshToken: data.refreshToken
            // };
        })
        .catch(error => {
            alert("로그인에 실패했습니다.");
            console.log(error);

            return null;
        });
}

export function checkApi() {
    axios.get('/api/auth/check')
        .then(res => {
            const data = res.data;
            const id = data.data;

            store.commit('setAccount', id);

            return id;
        })
        .catch(error => {
            console.log(error);

            store.commit('setAccount', 0);
        });
}
