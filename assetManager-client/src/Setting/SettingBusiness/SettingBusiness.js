import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import CustomModal from '../../components/Modal/CustomModal'
import customAxios from '../../customAxios'
import useStore from '../../mobx/useStore'

import './SettingBusiness.css'

const SettingBusiness = (props) => {

    const { loginUser } = useStore()

    const [ businessNames, setBusinessNames ] = useState([])
    const [ newBusinessName, setNewBusinessName ] = useState('')

    const [ addButtonHoverStatus, setAddButtonHoverStatus ] = useState(false)

    useEffect(() => {
        retrieveAllBusinessNames()
    }, [])

    /**
     * 소유한 상호명(닉네임)을 전부 불러온다
     */
    const retrieveAllBusinessNames = () =>{
        customAxios("/business/readAll", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                setBusinessNames(response.businessNames)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUserId
        })
    }

    /**
     * 상호명 추가 쿼리
     * 
     * @param callback Modal를 닫는 콜백
     */
    const addBusinessNameHandler = (callback) => {

        if ( newBusinessName === '' ) {
            alert('새로운 상호명을 입력해주세요!')
            return
        }

        customAxios("/business/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllBusinessNames()  // refresh
                setNewBusinessName('')      // 초기화
                callback()                  // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessName: newBusinessName
        })
    }

    const deleteBusinessNameHandler = (deleteBusinessName) => {
        return (callback) => customAxios("/business/delete", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllBusinessNames()  // refresh
                callback()                  // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessName: deleteBusinessName
        })
    }

    /**
     * 상호명 리스트를 랜더링한다
     */
    const renderBusinessNames = () => {
        return businessNames.map(businessNameJson => (
            <li
                key={businessNameJson.seq}
                className='SettingBusiness__list__item'>
                    <p>{businessNameJson.businessName}</p>
                    <div className='SettingBusiness__list__buttons'>
                        <button>Edit</button>
                        <CustomModal
                            modalTitle={`삭제할 상호명: ${businessNameJson.businessName}`}
                            toggleButton={
                                (
                                    <button>Delete</button>
                                )
                            }
                            okButtonClickedHandler={deleteBusinessNameHandler(businessNameJson.businessName)} >
                                <p>정말 삭제할까요?</p>
                        </CustomModal>
                    </div>
            </li>
        ))
    }

    return (
        <section className='SettingBusiness'>
            <div className='SettingBusiness__header'>
                <h1>상호명(닉네임) 설정</h1>
                <CustomModal
                    modalTitle='상호명(닉네임) 추가'
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
                                ...( addButtonHoverStatus &&
                                        {
                                            "backgroundColor": "#008CBA",
                                            "color": "white"
                                        }
                                    )
                            }}>
                                ADD
                        </button>)
                    }
                    okButtonClickedHandler={addBusinessNameHandler} >
                        <label
                            htmlFor="businessName"
                            style={{
                                "fontSize": "1.4em"
                            }}>
                                상호명(닉네임):
                        </label>
                        <input
                            id="businessName"
                            type="text"
                            className="modal__businessName"
                            style={{
                                "marginLeft": "12px",
                                "width": "50vw",
                                "height": "4vh",
                                "fontSize": "1.2em"
                            }}
                            value={newBusinessName}
                            onChange={(event) => {setNewBusinessName(event.target.value)}}
                            placeholder="상호명(닉네임)"/>
                </CustomModal>
                
            </div>
            
            <div className='SettingBusiness__list'>
                <ul>
                    { renderBusinessNames() }
                </ul>
            </div>
        </section>
    )
}

export default SettingBusiness