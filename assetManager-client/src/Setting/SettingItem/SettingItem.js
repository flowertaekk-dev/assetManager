import React, { useState } from 'react'

import CustomModal from '../../components/Modal/CustomModal'

import './SettingItem.css'

const SettingItem = () => {

    const [ addButtonHoverStatus, setAddButtonHoverStatus ] = useState(false)

    return (
        <section className='SettingItem'>
            
            <div className='SettingItem__header'>
                <h1>메뉴 설정</h1>
                <CustomModal
                    modalTitle='메뉴 추가'
                    toggleButton={
                        (<button
                            onMouseEnter={() => setAddButtonHoverStatus(true)}
                            onMouseLeave={() => setAddButtonHoverStatus(false)}
                            style={{
                                "backgroundColor": "white",
                                "border": "2px solid #008CBA",
                                "color": "black",
                                "padding": "8px 24px",
                                "textAlign": "center",
                                "textDecoration": "none",
                                "display": "inline-block",
                                "fontSize": "1.3vw",
                                "margin": "4px 2px",
                                "WebkitTransitionDuration": "0.4s",
                                "cursor": "pointer",
                                ...( addButtonHoverStatus && {
                                        "backgroundColor": "#008CBA",
                                        "color": "white"} )
                            }}>
                                ADD
                        </button>)
                    }
                    okButtonClickedHandler={() => console.log('dummy')} >
                </CustomModal>
            </div>
            
            <div className='SettingItem__list'>
                <ul>
                    <li className='SettingItem__list__item'>
                        <p>삼겹살</p>
                        <p>5000원</p>
                    </li>
                    <li className='SettingItem__list__item'>
                        <p>육회</p>
                        <p>12000원</p>
                    </li>
                    <li className='SettingItem__list__item'>
                        <p>함흥냉면</p>
                        <p>9000원</p>
                    </li>
                </ul>
            </div>

        </section>
    )
}

export default SettingItem