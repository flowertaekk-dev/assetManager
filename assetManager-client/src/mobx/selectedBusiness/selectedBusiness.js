import { observable } from 'mobx'

const selectedBusiness = observable({
    selectedBusinessId: window.localStorage.getItem('selectedBusinessId'),
    updateSelectedBusinessId(selectedBusinessId) {
        this.selectedBusinessId = selectedBusinessId
        window.localStorage.setItem('selectedBusinessId', selectedBusinessId)
    },
    deleteSelectedBusinessId() {
        this.selectedBusinessId = undefined
    },
})

export { selectedBusiness }