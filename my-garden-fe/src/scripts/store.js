import {createStore} from 'vuex'

export const store = createStore({
    state() {
        return {
            account: {
                accessToken: '',
                refreshToken: ''
            }
        }
    },
    mutations: {
        setToken(state, payload) {
            state.account.accessToken = payload.accessToken;
            state.account.refreshToken = payload.refreshToken;
        },
    },
    actions: {
        initializeTokenFromSessionStorage({commit}) {
            const token = JSON.parse(sessionStorage.getItem('token'));

            if (!token) return;
            commit('setToken', token);
        }
    },
    getters: {
        getAccessToken(state) {
            return state.account.accessToken;
        },
        getRefreshToken(state) {
            return state.account.refreshToken;
        }
    }
});
