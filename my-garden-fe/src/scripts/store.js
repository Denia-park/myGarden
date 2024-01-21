import {createStore} from 'vuex'
import {parseJwt} from "@/scripts/parseJwt.js";
import {getTodayDate} from "@/components/dailyRoutine/api/api.js";

export const store = createStore({
    state() {
        return {
            account: {
                accessToken: '',
                refreshToken: '',
                roles: []
            },
            editBlock: {},
            viewDate: getTodayDate(),
            colorMap: {
                '운동': '#b23f3f',
                '수면': '#a0a0a0',
                '식사': '#70db70',
                '공부': '#ffdb4d',
                '휴식': '#4de4ff',
                '게임': '#e76c0c',
                '기타': '#cd4dff',
            }
        }
    },
    mutations: {
        setToken(state, payload) {
            state.account.accessToken = payload.accessToken;
            state.account.refreshToken = payload.refreshToken;
        },
        clearToken(state) {
            state.account.accessToken = '';
            state.account.refreshToken = '';
        },
        setRoles(state, payload) {
            state.account.roles = Array.isArray(payload) ? payload : [];
        },
        setEditBlock(state, payload) {
            state.editBlock = payload;
        },
        setViewDate(state, payload) {
            state.viewDate = payload;
        }
    },
    actions: {
        initializeTokenFromSessionStorage({commit}) {
            try {
                const tokenString = sessionStorage.getItem('token');
                const token = tokenString ? JSON.parse(tokenString) : null;

                if (token?.accessToken && token.refreshToken) {
                    commit('setToken', token);
                }
            } catch (error) {
                console.error("Error parsing token from session storage:", error);
            }
        },
        initializeAuthenticationRoles({commit}, context) {
            const accessToken = context.getters.getAccessToken;

            if (!accessToken) return;

            const payload = parseJwt(accessToken);
            const roles = payload.roles.split(',');

            commit('setRoles', roles);
        }
    },
    getters: {
        getAccessToken(state) {
            return state.account.accessToken;
        },
        getRefreshToken(state) {
            return state.account.refreshToken;
        },
        getColors(state) {
            return state.colorMap;
        },
        getEditBlock(state) {
            return state.editBlock;
        },
        getViewDate(state) {
            return state.viewDate;
        }
    }
});
