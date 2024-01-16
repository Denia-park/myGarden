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
})
