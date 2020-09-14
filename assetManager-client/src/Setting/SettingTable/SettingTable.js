import React from 'react'

import './SettingTable.css'

const SettingTable = () => {
    return (
        <section className='SettingTable'>
            
            <div className='SettingTable__header'>
                <h1>테이블 설정</h1>
            </div>

            <div className='SettingTable__list'>
                <ul>
                    <li className='SettingTable__list__item'>
                        <p>개수: 10</p>
                        <button>Edit</button>
                    </li>
                </ul>
            </div>
        </section>
    )
}

export default SettingTable