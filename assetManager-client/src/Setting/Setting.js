import React from 'react'

import SettingTable from './SettingTable/SettingTable'
import SettingItem from './SettingItem/SettingItem'

import './Setting.css'

const Setting = () => {
    return (
        <section className='Setting'>
            
            {/* Table 개수 설정 */}
            <SettingTable />


            {/* 메뉴 설정: 메뉴명 단가 */}
            <SettingItem />
            
        </section>
    )
}

export default Setting