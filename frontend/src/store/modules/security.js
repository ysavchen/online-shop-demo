import axios from 'axios'

const state = {
    token: null,
    user: null
}

const getters = {
    token: state => state.token,
    user: state => state.user,
    isAuthenticated: state => state.token !== null
}

const actions = {
    register: async ({ commit, dispatch }, user) => {
        const response = await axios({
            method: 'post',
            url: '/v1/register',
            data: user,
            headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        commit('SET_TOKEN', response.data.token)
        commit('SET_USER', response.data.user)
        dispatch('setLogoutTimer', response.data.tokenExpiration)
    },

    login: async ({ commit, dispatch }, user) => {
        const response = await axios({
            method: 'post',
            url: '/v1/login',
            data: user,
            headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        commit('SET_TOKEN', response.data.token)
        commit('SET_USER', response.data.user)
        dispatch('setLogoutTimer', response.data.tokenExpiration)
    },

    logout: ({ commit }) => {
        commit('SET_TOKEN', null)
        commit('SET_USER', null)
    },

    setLogoutTimer({ commit }, tokenExpiration) {
        setTimeout(() => {
            commit('SET_TOKEN', null)
            commit('SET_USER', null)
        }, tokenExpiration)
    }
}

const mutations = {
    'SET_TOKEN'(state, token) {
        state.token = token
    },
    'SET_USER'(state, user) {
        state.user = user
    }
}

export default {
    state,
    getters,
    actions,
    mutations,
}