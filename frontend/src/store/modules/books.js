import axios from 'axios'

const state = {
    books: []
}

const getters = {
    books: state => state.books,
    bookWithId: state => id => {
        return state.books.find(book => book.id === Number.parseInt(id))
    }
}

const actions = {
    getBooks: ({ commit }) => {
        axios.get('/v1/books')
            .then((response) => {
                commit('ADD_BOOKS', response.data)
            })
    },
    getBookById: async ({ commit }, id) => {
        const response = await axios.get(`/v1/books/${id}`)
        commit('ADD_BOOK', response.data)
    }
}

const mutations = {
    'ADD_BOOKS'(state, books) {
        state.books = books
    },
    'ADD_BOOK'(state, book) {
        state.books.push(book)
    }
}

export default {
    state,
    getters,
    actions,
    mutations,
}