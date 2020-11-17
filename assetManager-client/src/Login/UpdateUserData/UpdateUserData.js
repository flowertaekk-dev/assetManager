import React, { useState } from 'react';

import ValidatePassword from './ValidatePassword/ValidatePassword'

import './UpdateUserData.css'

const UpdateUserData = () => {

    const [validateStatus, setValidateStatus] = useState(false)

    return (
        <section className='UpdateUserData'>
            {!validateStatus && <ValidatePassword />}
            {validateStatus && <h1>UpdateUserData</h1>}
            {/* <h1>UpdateUserData</h1> */}

        </section>
    )
}

export default UpdateUserData