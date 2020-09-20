import { observable } from 'mobx'

const selectedBusiness = observable({
    selectedBusiness: window.localStorage.getItem('selectedBusiness'),
    updateSelectedBusiness(selectedBusiness) {
        this.selectedBusiness = selectedBusiness
        window.localStorage.setItem('selectedBusiness', selectedBusiness)
    },
    deleteSelectedBusiness() {
        this.selectedBusiness = undefined
    },
})

export { selectedBusiness }