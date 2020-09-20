import { loginUser } from './loginUser/loginUser'
import { selectedBusiness } from './selectedBusiness/selectedBusiness'

function useStore() {
    return { loginUser, selectedBusiness }
}

export default useStore