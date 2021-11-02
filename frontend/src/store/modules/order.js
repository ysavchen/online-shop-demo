import axios from 'axios'

const state = {
    order: null
}

const getters = {
    order: state => state.order
}

const actions = {
    saveOrder: async ({ commit }, order) => {
        const response = await axios({
            method: 'post',
            url: '/v1/orders',
            data: order,
            headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        order.id = response.data.id
        order.createdAt = new Date(response.data.createdAt)
        commit('SAVE_ORDER', order)
    }
}

const mutations = {
    'SAVE_ORDER'(state, order) {
        state.order = order
    }
}

export default {
    state,
    getters,
    actions,
    mutations,
}