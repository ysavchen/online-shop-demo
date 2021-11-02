const state = {
    cart: []
}

const getters = {
    cart: state => state.cart
}

const actions = {
    addToCart: ({ commit }, book) => {
        commit('ADD_TO_CART', book)
    },
    deleteFromCart: ({ commit }, book) => {
        commit('DELETE_FROM_CART', book)
    },
    emptyCart: ({ commit }) => {
        commit('EMPTY_CART')
    }
}

const mutations = {
    'ADD_TO_CART'(state, book) {
        state.cart.push(book)
    },
    'DELETE_FROM_CART'(state, book) {
        state.cart.splice(state.cart.indexOf(book), 1)
    },
    'EMPTY_CART'(state) {
        state.cart = []
    },
}

export default {
    state,
    getters,
    actions,
    mutations,
}