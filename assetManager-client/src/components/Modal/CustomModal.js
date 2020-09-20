import React, { useState } from 'react';
import Modal from 'react-modal'

import './CustomModal.css'

Modal.setAppElement('#root')

const CustomModal = (props) => {

  const [isOpen, setIsOpen] = useState(false)

  const openModal = () => {
    setIsOpen(true)
  }

  const closeModal = () => {
    setIsOpen(false)
  }

  const okButtonClickedHandler = () => {
    let preCheckResult = true;

    // PreCheck handler가 등록되어 있으면 preCheck 실행
    if (props.preCheckHandler) {
      preCheckResult = props.preCheckHandler()
    }

    if (preCheckResult) {
      props.okButtonClickedHandler(closeModal)
    }
  }

  const cancelButtonClickedHandler = () => {
    // Cancel handler가 등록되어 있으면 Handler 실행
    if (props.cancelButtonClickedHandler) {
      props.cancelButtonClickedHandler(closeModal)
      return
    }

    closeModal()
  }

  return (
      <div className='CustomModal'>
        <span onClick={openModal}>
          {props.toggleButton}
        </span>

        <Modal
          isOpen={isOpen}
          onRequestClose={ cancelButtonClickedHandler }
          contentLabel={'Hello Modal!'}
          className='CustomModal__modal'
          overlayClassName='CustomModal__overlay'
          closeTimeoutMS={500}
        >
          <h1 className='modal__title'>{props.modalTitle}</h1>

          <div className="modal__body">
            {props.children}
          </div>

          <div className='modal__buttons'>
            <button className='modal__ok__btn' onClick={ okButtonClickedHandler }>OK</button>
            <button className='modal__cancel__btn' onClick={ cancelButtonClickedHandler }>CANCEL</button>
          </div>
        </Modal>

      </div>
  )
}

export default CustomModal