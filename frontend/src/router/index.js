import Vue from 'vue'
import VueRouter from 'vue-router'
import BooksView from '@/views/BooksView.vue'
import CheckoutForm from '@/views/CheckoutForm.vue'
import BookDetails from '@/views/BookDetails.vue'
import OrderView from '@/views/OrderView.vue'
import UserOrders from '@/views/UserOrders.vue'
import security from '@/store/modules/security'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'books', component: BooksView },
  { path: '/books/:id', name: 'bookDetails', component: BookDetails },
  { path: '/checkout', name: 'checkout', component: CheckoutForm },
  { path: '/order/:id', name: 'order', component: OrderView },
  {
    path: '/users/:id/orders', name: 'userOrders', component: UserOrders,
    beforeEnter(to, from, next) {
      if (security.state.token) {
        next()
      } else {
        next('/')
      }
    }
  },
  { path: '*', redirect: '/' }
]

export default new VueRouter({
  mode: 'history',
  routes
})