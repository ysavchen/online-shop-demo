export default {
    data: () => ({
        rules: {
            required: value => !!value || 'Required.',
            minChars: v => (v && v.length >= 8) || 'Min 8 characters',
            emailValidity: v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
        }
    }),
}