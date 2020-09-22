import React from 'react'

import SettingBusiness from './SettingBusiness/SettingBusiness'
import SettingTable from './SettingTable/SettingTable'
import SettingMenu from './SettingMenu/SettingMenu'

import './Setting.css'

const Setting = () => {

    return (
        <section className='Setting'>

            {/* 상호명 설정 및 선택 */}
            <SettingBusiness />
            
            {/* Table 개수 설정 */}
            <SettingTable />

            {/* 메뉴 설정: 메뉴명 단가 */}
            <SettingMenu />
            
        </section>
    )
}

export default Setting