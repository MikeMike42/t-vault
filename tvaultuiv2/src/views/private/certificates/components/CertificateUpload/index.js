import { useState } from 'react';
import { Upload, X, File, CheckCircle, CloudUploadIcon } from 'lucide-react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import { Backdrop, Button, InputLabel } from '@material-ui/core';
import Fade from '@material-ui/core/Fade';
import ButtonComponent from '../../../../../components/FormFields/ActionButton';
import {
	GlobalModalWrapper,
	RequiredCircle
} from '../../../../../styles/GlobalStyles';
import apiService from '../../apiService';
import TextFieldComponent from '../../../../../components/FormFields/TextField';
import CertificateHeader from '../CertificateHeader';

const InputFieldLabelWrapper = styled.div`
	margin-bottom: 2rem;
	position: ${(props) => (props.postion ? 'relative' : '')};
	.MuiSelect-icon {
		top: auto;
		color: ${(props) => props.theme.customColor.primary.color};
	}
`;

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

const StyledModal = styled(Modal)`
		@-moz-document url-prefix() {
			.MuiBackdrop-root {
				position: absolute;
				height: 95rem;
			}
		}
	`;

	const FooterText = styled.p`
		font-size: 16px;
		color: white;
	`;

	const InputEndWrap = styled.div`
		display: flex;
	`;

	
	const EndingBox = styled.div`
		background-color: ${(props) =>
			props.disabled ? "rgba(0, 0, 0, 0.12)" : props.theme.customColor.primary.backgroundColor};
		color: ${(props) => props.theme.customColor.primary.color};
		width: ${(props) => props.width};
		display: flex;
		align-items: center;
		height: 5rem;
	`;

	const VisuallyHiddenInput = styled('input')({
		clip: 'rect(0 0 0 0)',
		clipPath: 'inset(50%)',
		height: 1,
		overflow: 'hidden',
		position: 'absolute',
		bottom: 0,
		left: 0,
		whiteSpace: 'nowrap',
		width: 1,
	});


export default function CertificateFileUploader(props) {
	const [open, setOpen] = useState(true);
	const [files, setFiles] = useState([]);
	const [isDragging, setIsDragging] = useState(false);
	const classes = useStyles();
	const [keystoreName, setKeystoreName] = useState('gwcpnonprod.aceclublink.com_');

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

		for (let i = 0; i < e.dataTransfer.files.length; i++) {
			var droppedFile = e.dataTransfer.files[i]
			if (droppedFile) {
				const reader = new FileReader();
				reader.fileName = droppedFile.name

				reader.onload = (event) => {
					const fileContent = event.target.result;
					setFiles((previous) => [...previous, {fileName: event.target.fileName, content: fileContent}]);
				}

				reader.readAsText(droppedFile);
			}
		}
	
	};

	const handleFileChange = (e) => {
		var files = e.target.files

		for (let i = 0; i < files.length; i++) {
			var droppedFile = files[i]
			if (droppedFile) {
				const reader = new FileReader();
				reader.fileName = droppedFile.name

				reader.onload = (event) => {
					const fileContent = event.target.result;
					setFiles((previous) => [...previous, {fileName: event.target.fileName, content: fileContent}]);
				}

				reader.readAsText(droppedFile);
			}
		}
	};

	const onKeyStoreNameChange = (e) => {
		setKeystoreName(e.target.value);
	};

	const handleRemove = () => {
		setFiles([]);
	};

	const formatFileSize = (bytes) => {
		if (bytes === 0) return '0 Bytes';
		const k = 1024;
		const sizes = ['Bytes', 'KB', 'MB', 'GB'];
		const i = Math.floor(Math.log(bytes) / Math.log(k));
		return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
	};

	const uploadFiles = () => {
		apiService.uploadKeystore({uploads: files, keystoreName: keystoreName + '.jks'})
		.then(async (res) => {
				props.setToastResponse(1)
				props.setToastMessage('Successfully created keystore')
				setOpen(false)
				setTimeout(function() {
					window.location.reload()
				}, 1000);
      })
      .catch((err) => {
        props.setToastResponse(-1)
				props.setToastMessage('An error occurred while creating keystore')
				setOpen(false)
				setTimeout(function() {
					window.location.reload()
				}, 1000);
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
					<CertificateHeader />
					<InputFieldLabelWrapper>
						<div style={{ width: '80%' }}>
							<InputLabel>
								Keystore Name
								<RequiredCircle margin="1.3rem" />
							</InputLabel>
							<InputEndWrap>
								<TextFieldComponent
									value={keystoreName}
									placeholder="Enter a keystore name..."
									fullWidth
									name="keystoreName"
									onChange={(e) => {
										onKeyStoreNameChange(e)
									}}
								/>
								<EndingBox width="14rem">. jks</EndingBox>
							</InputEndWrap>
						</div>
					</InputFieldLabelWrapper>
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
								{files.length === 0 ? (
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
											Drag and Drop Certificates
										</h3>
										<p className="text-gray-400 text-lg mb-4" style={{ fontSize: '18px' }}>
											or
										</p>
										<Button
											component="label"
											role={undefined}
											variant="contained"
											color='secondary'
											tabIndex={-1}
											startIcon={<CloudUploadIcon />}
										>
											Upload files
											<VisuallyHiddenInput
												type="file"
												onChange={(event) => handleFileChange(event)}
												multiple
											/>
										</Button>
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
														{/* Update with filename */}
													</p>
													<p className="text-gray-400 text-sm">
														{/* Update with file size{formatFileSize(file.size)} */}
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
												label="Upload Selected Files"
												color="secondary"
												onClick={() => {uploadFiles()}}
											/>
										</div>
									</div>
								)}
								<FooterText>
									Supported format(s): pem, crt, key
								</FooterText>
							</div>
						</div>
					</div>
				</GlobalModalWrapper>
			</Fade>
		</StyledModal>
	);
}