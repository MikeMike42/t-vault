import { useState } from 'react';
import { Upload, X, File, CheckCircle } from 'lucide-react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import { Backdrop, Button } from '@material-ui/core';
import Fade from '@material-ui/core/Fade';
import ButtonComponent from '../../../../../components/FormFields/ActionButton';
import {
	GlobalModalWrapper
} from '../../../../../styles/GlobalStyles';
import apiService from '../../apiService';

export default function CertificateFileUploader() {
	const useStyles = makeStyles((theme) => ({
		select: {
			'&.MuiFilledInput-root.Mui-focused': {
				backgroundColor: '#fff',
			},
		},
		dropdownStyle: {
			backgroundColor: '#fff',
			maxHeight: '20rem',
		},
		modal: {
			display: 'flex',
			alignItems: 'center',
			justifyContent: 'center',
			overflowY: 'auto',
			padding: '10rem 0',
			[theme.breakpoints.down('xs')]: {
				alignItems: 'unset',
				justifyContent: 'unset',
				padding: '0',
				height: '100%',
			},
		},
	}));

	const [open, setOpen] = useState(true);
	const [file, setFile] = useState(null);
	const [isDragging, setIsDragging] = useState(false);
	const classes = useStyles();

	const handleClose = () => {
		setOpen(false)
		window.location.href = '/certificates/';
	}

	const handleDragOver = (e) => {
		e.preventDefault();
		setIsDragging(true);
	};

	const handleDragLeave = (e) => {
		e.preventDefault();
		setIsDragging(false);
	};

	const handleDrop = (e) => {
		e.preventDefault();
		setIsDragging(false);

		const droppedFile = e.dataTransfer.files[0];
		if (droppedFile) {
			const reader = new FileReader();

			reader.onload = (event) => {
				const fileContent = event.target.result;
				setFile(fileContent);
			}

			reader.readAsText(droppedFile);
			// reader.readAsArrayBuffer(droppedFile);
		}
	};

	const handleFileChange = (e) => {
		const selectedFile = e.target.files[0];
		if (selectedFile) {
			setFile(selectedFile);
		}
	};

	const handleRemove = () => {
		setFile(null);
	};

	const StyledModal = styled(Modal)`
		@-moz-document url-prefix() {
			.MuiBackdrop-root {
				position: absolute;
				height: 95rem;
			}
		}
	`;

	const FooterText = styled.p`
		font-size: 18px;
		color: white;
	`;

	const formatFileSize = (bytes) => {
		if (bytes === 0) return '0 Bytes';
		const k = 1024;
		const sizes = ['Bytes', 'KB', 'MB', 'GB'];
		const i = Math.floor(Math.log(bytes) / Math.log(k));
		return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
	};

	const uploadFile = () => {
		const payload = {
      fileName: 'testing.jks',
			content: file
    };

		console.log('payload', payload)

		apiService.uploadKeystore(payload)
		.then(async (res) => {
        console.log('res', res)
      })
      .catch((err) => {
        console.error("shits fucked", err)
      });
	}

	return (
		<StyledModal
			aria-labelledby="transition-modal-title"
			aria-describedby="transition-modal-description"
			className={classes.modal}
			open={open}
			onClose={() => handleClose()}
			closeAfterTransition
			BackdropComponent={Backdrop}
			BackdropProps={{
				timeout: 500,
			}}
		>
			<Fade in={open}>
				<GlobalModalWrapper>
					<div className="min-h-screen bg-gray-900 flex items-center justify-center p-4">
						<div className="w-full max-w-md">

							<div
								onDragOver={handleDragOver}
								onDragLeave={handleDragLeave}
								onDrop={handleDrop}
								className={`relative border-2 border-dashed rounded-lg p-8 transition-all duration-300 ${isDragging
										? 'border-[#e20074] bg-gray-800'
										: 'border-gray-700 bg-gray-800/50'
									}`}
							>
								{!file ? (
									<div 
										className="text-center" 
										style={{ 
											border: '3px dashed white',
											borderRadius: '1rem',
											padding: '1rem',
											textAlign: 'center',
											paddingTop: '2rem',
											paddingBottom: '2rem'
										}}
									>
										<div className="flex justify-center mb-4">
											<div className="p-4 bg-gray-700 rounded-full">
												<Upload className="w-8 h-8 text-[#e20074]" />
											</div>
										</div>

										<h3 className="text-lg font-semibold text-white mb-2">
											Drag and Drop Keystore
										</h3>
										<p className="text-gray-400 text-lg mb-4" style={{ fontSize: '18px' }}>
											or
										</p>

										<ButtonComponent
											type="file" 
											onChange={handleFileChange}
                      label="Upload File"
                      color="secondary"
                      // onClick={() => {}}
                    />
									</div>
								) : (
									<div className="space-y-4">
										<div className="flex items-center justify-between p-4 bg-gray-700 rounded-lg">
											<div className="flex items-center space-x-3 flex-1 min-w-0">
												<div className="p-2 bg-gray-600 rounded">
													<File className="w-6 h-6 text-[#e20074]" />
												</div>
												<div className="flex-1 min-w-0">
													<p className="text-white font-medium truncate">
														{file.name}
													</p>
													<p className="text-gray-400 text-sm">
														{formatFileSize(file.size)}
													</p>
												</div>
											</div>
											<button
												onClick={handleRemove}
												className="ml-3 p-2 hover:bg-gray-600 rounded-full transition-colors duration-200"
											>
												<X className="w-5 h-5 text-gray-400 hover:text-white" />
											</button>
										</div>

										<div style={{ marginTop: '1rem' }} className="flex items-center justify-center space-x-2 text-[#e20074]">
											<CheckCircle className="w-5 h-5" />
											<span className="font-medium" style={{ marginLeft: '1rem' }}>File ready to upload</span>
										</div>

										<div style={{ marginTop: '1rem' }}>
											<ButtonComponent
												type="file" 
												label="Upload Selected File"
												color="secondary"
												onClick={() => {uploadFile()}}
											/>
										</div>
									</div>
								)}
								<FooterText>
									Supported format(s): .jks
								</FooterText>
							</div>
						</div>
					</div>
				</GlobalModalWrapper>
			</Fade>
		</StyledModal>
	);
}