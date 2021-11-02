import numeral from "numeral"

export default {
    methods: {
        totalPrice(books) {
            let totalPrice = numeral(0.0);
            for (const book of books) {
                totalPrice = totalPrice.add(book.price)
            }
            return totalPrice.value()
        }
    }
}