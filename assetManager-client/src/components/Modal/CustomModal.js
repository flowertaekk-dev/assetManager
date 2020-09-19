import React, { useState } from 'react';
import Modal from 'react-modal'

import './CustomModal.css'

Modal.setAppElement('#root')

const CustomModal = (props) => {

  const [isOpen, setIsOpen] = useState(false)

  const toggleModal = () => {
    setIsOpen(!isOpen)
  }    

  return (
      <div className='CustomModal'>
        <button
          onClick={toggleModal}
          className='CustomModal__toggleButton'>
            {props.toggleButtonText}
        </button>

        <Modal
          isOpen={isOpen}
          onRequestClose={toggleModal}
          contentLabel={'Hello Modal!'}
          className='CustomModal__modal'
          overlayClassName='CustomModal__overlay'
          closeTimeoutMS={500}
        >
          <div>My modal dialog.</div>
          <button onClick={toggleModal}>Close modal</button>
        </Modal>

      </div>
  )
}

export default CustomModal