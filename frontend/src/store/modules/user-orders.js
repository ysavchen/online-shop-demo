import axios from 'axios'

const state = {
    userOrders: []
}

const getters = {
    userOrders: state => state.userOrders
}

const actions = {
    getUserOrders: async ({ commit }, reqData) => {
        const response = await axios({
            method: 'get',
            url: `/v1/users/${reqData.userId}/orders`,
            headers: { 'Authorization': `Bearer ${reqData.userToken}` }
        })
        commit('SAVE_USER_ORDERS', response.data)
    }
}

const mutations = {
    'SAVE_USER_ORDERS'(state, userOrders) {
        state.userOrders = userOrders
    }
}

export default {
    state,
    getters,
    actions,
    mutations,
}