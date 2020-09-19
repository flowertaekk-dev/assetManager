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
    props.okButtonClickedHandler(() => setIsOpen(false))
  }

  return (
      <div className='CustomModal'>
        <button
          onClick={openModal}
          className='CustomModal__toggleButton'>
            {props.toggleButtonText}
        </button>

        <Modal
          isOpen={isOpen}
          onRequestClose={closeModal}
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
            <button className='modal__ok__btn' onClick={okButtonClickedHandler}>OK</button>
            <button className='modal__cancel__btn' onClick={closeModal}>CANCEL</button>
          </div>
        </Modal>

      </div>
  )
}

export default CustomModal