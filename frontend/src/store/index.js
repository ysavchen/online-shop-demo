import Vue from 'vue'
import Vuex from 'vuex'
import books from './modules/books'
import cart from './modules/cart'
import order from './modules/order'
import security from './modules/security'
import userOrders from './modules/user-orders'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    books,
    cart,
    order,
    security,
    userOrders
  }
})
