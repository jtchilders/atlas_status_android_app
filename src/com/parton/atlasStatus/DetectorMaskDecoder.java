package com.parton.atlasStatus;


public class DetectorMaskDecoder {
//	private static final String TAG = "DetectorMaskDecoder";
	private long detMask;

	public DetectorMaskDecoder(String detMask){
		this.detMask = Long.decode(detMask);
	}

	public boolean isIncluded(String hw_name){
//		Log.v(TAG,"isIncluded: hw_name: "+hw_name+" detMask: "+detMask);

		// Pixel is bits 0,1,2
		if (hw_name.contains("PIXEL") && ((detMask & (long)0x7) > 0))
			return true; 

		// bit three for "OTHER"

		// SCT is bits 4,5,6,7
		else if (hw_name.contains("SCT") && ((detMask & ((long)0xF << 4)) > 0))
			return true; 

		// TRT is bits 8,9,10,11
		else if (hw_name.contains("TRT") && ((detMask & ((long)0xF << 8)) > 0))
			return true; 

		// Is LAr included? bits 12 - 19
		else if (hw_name.contains("LAR") && ((detMask & ((long)0xFF << 12)) > 0))
			return true; 

		// Is Tile included? bits 20 - 23
		else if (hw_name.contains("TILECAL") && ((detMask & ((long)0xF << 20)) > 0))
			return true;

		// MDT is bits 24 - 27
		else if (hw_name.contains("MUON_MDT") && ((detMask & ((long)0xF << 24)) > 0))
			return true; 

		// RPC is bits 28,29
		else if (hw_name.contains("MUON_RPC") && ((detMask & ((long)0x3 << 28)) > 0))
			return true;

		// TGC is bits 30,31 
		else if (hw_name.contains("MUON_TGC") && ((detMask & ((long)0x3 << 30)) > 0))
			return true;

		// CSC is bits 32,33 (or 0,1 of high word)
		else if (hw_name.contains("MUON_CSC") && ((detMask & ((long)0x3 << 32)) > 0))
			return true;

		// L1Calo bits are 34,35,36,37,38 so use high bit word (2-6)
		else if (hw_name.contains("TDAQ_CALO") && ((detMask & ((long)0x1f << 34)) > 0))
			return true;

		// MuCTPI and CTP bits are 39 and 40 so use high bit word (7 - 8)
		// The id TDAQ_L1CT is used for either CTP or MUCTPI.
		else if (hw_name.contains("TDAQ_MUON_CTP_INTERFACE") && ((detMask & ((long)0x1 << 39)) > 0))
			return true; //) != 0;

		else if (hw_name.contains("TDAQ_CTP") && ((detMask & ((long)0x1 << 40)) > 0))
			return true;

		else if (hw_name.contains("TDAQ_L1CT") && ((detMask & ((long)0x3 << 39)) > 0))
			return true;

		// HLT are 41 - 45 (L2SV, SFI, SFO, LVL2, EF)
		else if (hw_name.contains("HLT") && ((detMask & ((long)0x1f << 41)) > 0))
			return true;

		// Forward BCM is 46 
		else if (hw_name.contains("FORWARD_BCM") && ((detMask & ((long)0x1 << 46)) > 0))
			return true;

		// Forward LUCID is 47 
		else if (hw_name.contains("FORWARD_LUCID") && ((detMask & ((long)0x1 << 47)) > 0))
			return true;

		// Forward ZDC is 48
		else if (hw_name.contains("FORWARD_ZDC") && ((detMask & ((long)0x1 << 48)) > 0))
			return true;

		// Forward ALPHA is 49
		else if (hw_name.contains("FORWARD_ALPHA") && ((detMask & ((long)0x1 << 49)) > 0))
			return true;

		// bit 50 = TRT_ANCILLARY_CRATE
		// bit 51 = TILECAL_LASER_CRATE
		// bit 52 = MUON_ANCILLARY_CRATE
		// bit 53 = TDAQ_BEAM_CRATE
		// bit 54 = TDAQ_FTK

		/////////////////////////////////////////////////////////////////////////////
		// Now get the detailed status of ID subdetectors for the ID view.
		/////////////////////////////////////////////////////////////////////////////

		// Pixel is bits 0,1,2
		else if (hw_name.contains("PIXEL_BARREL") && ((detMask & ((long)0x1 << 0)) > 0))
			return true;

		else if (hw_name.contains("PIXEL_DISK") && ((detMask & ((long)0x1 << 1)) > 0))
			return true; 

		else if (hw_name.contains("PIXEL_B_LAYER") && ((detMask & ((long)0x1 << 2)) > 0))
			return true;

		// SCT is bits 4,5,6,7
		else if (hw_name.contains("SCT_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 4)) > 0))
			return true; 

		else if (hw_name.contains("SCT_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 5)) > 0))
			return true; 

		else if (hw_name.contains("SCT_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 6)) > 0))
			return true;

		else if (hw_name.contains("SCT_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 7)) > 0))
			return true;


		// TRT is bits 8,9,10,11
		else if (hw_name.contains("TRT_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 8)) > 0))
			return true; 

		else if (hw_name.contains("TRT_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 9)) > 0))
			return true; 

		else if (hw_name.contains("TRT_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 10)) > 0))
			return true; 

		else if (hw_name.contains("TRT_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 11)) > 0))
			return true;


		/////////////////////////////////////////////////////////////////////////////
		// Now get the detailed status of calo subdetectors for the calo view.
		/////////////////////////////////////////////////////////////////////////////

		// L1Calo bits are 34,35,36,37,38 so use high bit word

		// bit 34 of mask is PreProcessor
		else if (hw_name.contains("TDAQ_CALO_PREPROC") && ((detMask & ((long)0x1 << 34)) > 0))
			return true;

		// bit 35 of mask is Cluster Processor DAQ
		else if (hw_name.contains("TDAQ_CALO_CLUSTER_PROC_DAQ") && ((detMask & ((long)0x1 << 35)) > 0))
			return true;

		// bit 36 of mask is Cluster Processor RoI
		else if (hw_name.contains("TDAQ_CALO_CLUSTER_PROC_ROI") && ((detMask & ((long)0x1 << 36)) > 0))
			return true;

		// bit 37 of mask is Jet Processor DAQ
		else if (hw_name.contains("TDAQ_CALO_JET_PROC_DAQ") && ((detMask & ((long)0x1 << 37)) > 0))
			return true;

		// bit 38 of mask is Jet Processor RoI
		else if (hw_name.contains("TDAQ_CALO_JET_PROC_ROI") && ((detMask & ((long)0x1 << 38)) > 0))
			return true;

		// Is LAr included? bits 12 - 19

		// bit 12 of mask is larEMBA
		else if (hw_name.contains("LAR_EM_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 12)) > 0))
			return true;

		// bit 13 of mask is larEMBC
		else if (hw_name.contains("LAR_EM_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 13)) > 0))
			return true;

		// bit 14 of mask is larEMECA
		else if (hw_name.contains("LAR_EM_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 14)) > 0))
			return true;

		// bit 15 of mask is larEMECC
		else if (hw_name.contains("LAR_EM_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 15)) > 0))
			return true;

		// bit 16 of mask is larHECA
		else if (hw_name.contains("LAR_HAD_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 16)) > 0))
			return true;

		// bit 17 of mask is larHECC   
		else if (hw_name.contains("LAR_HAD_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 17)) > 0))
			return true;
		// bit 18 of mask is larFCALA
		else if (hw_name.contains("LAR_FCAL_A_SIDE") && ((detMask & ((long)0x1 << 18)) > 0))
			return true;

		// bit 19 of mask is larFCALC
		else if (hw_name.contains("LAR_FCAL_C_SIDE") && ((detMask & ((long)0x1 << 19)) > 0))
			return true;

		// Is Tile included? bits 20 - 23

		// bit 20 of mask is tileLBA
		else if (hw_name.contains("TILECAL_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 20)) > 0))
			return true;

		// bit 21 of mask is tileLBC
		else if (hw_name.contains("TILECAL_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 21)) > 0))
			return true;

		// bit 22 of mask is tileEBA
		else if (hw_name.contains("TILECAL_EXT_A_SIDE") && ((detMask & ((long)0x1 << 22)) > 0))
			return true;

		// bit 23 of mask is tileEBC
		else if (hw_name.contains("TILECAL_EXT_C_SIDE") && ((detMask & ((long)0x1 << 23)) > 0))
			return true;

		/////////////////////////////////////////////////////////////////////////////
		// Now get the detailed status of muon subdetectors for the muon view.
		/////////////////////////////////////////////////////////////////////////////

		// MDT is bits 24 - 27
		else if (hw_name.contains("MUON_MDT_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 24)) > 0))
			return true;

		else if (hw_name.contains("MUON_MDT_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 25)) > 0))
			return true;

		else if (hw_name.contains("MUON_MDT_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 26)) > 0))
			return true;

		else if (hw_name.contains("MUON_MDT_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 27)) > 0))
			return true;


		// RPC is bits 28,29
		else if (hw_name.contains("MUON_RPC_BARREL_A_SIDE") && ((detMask & ((long)0x1 << 28)) > 0))
			return true;

		else if (hw_name.contains("MUON_RPC_BARREL_C_SIDE") && ((detMask & ((long)0x1 << 29)) > 0))
			return true;


		// TGC is bits 30,31 
		else if (hw_name.contains("MUON_TGC_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 30)) > 0))
			return true;

		else if (hw_name.contains("MUON_TGC_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 31)) > 0))
			return true;


		// CSC is bits 32,33 (or 0,1 of high word)
		else if (hw_name.contains("MUON_CSC_ENDCAP_A_SIDE") && ((detMask & ((long)0x1 << 32)) > 0))
			return true;

		else if (hw_name.contains("MUON_CSC_ENDCAP_C_SIDE") && ((detMask & ((long)0x1 << 33)) > 0))
			return true;



		/////////////////////////////////////////////////////////////////////////////
		// Now get the detailed status of HLT subsystems for the HLT view.
		/////////////////////////////////////////////////////////////////////////////

		// L2SV is bits 41 
		else if (hw_name.contains("TDAQ_L2SV") && ((detMask & ((long)0x1 << 41)) > 0))
			return true;

		// SFI is bits 42
		else if (hw_name.contains("TDAQ_SFI") && ((detMask & ((long)0x1 << 42)) > 0))
			return true;

		// SFO is bits 43
		else if (hw_name.contains("TDAQ_SFO") && ((detMask & ((long)0x1 << 43)) > 0))
			return true;

		// LVL2 is bits 44
		else if (hw_name.contains("TDAQ_LVL2") && ((detMask & ((long)0x1 << 44)) > 0))
			return true;

		// EVENT_FILTER is bits 45
		else if (hw_name.contains("TDAQ_EVENT_FILTER") && ((detMask & ((long)0x1 << 45)) > 0))
			return true;

		else
			return false;
	}

}




