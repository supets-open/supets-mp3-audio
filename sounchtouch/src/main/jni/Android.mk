
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := soundtouch

LOCAL_SRC_FILES := \
	SoundTouch/AAFilter.cpp \
	SoundTouch/BPMDetect.cpp \
	SoundTouch/cpu_detect_x86.cpp \
	SoundTouch/FIFOSampleBuffer.cpp \
	SoundTouch/FIRFilter.cpp \
	SoundTouch/mmx_optimized.cpp \
	SoundTouch/PeakFinder.cpp \
	SoundTouch/RateTransposer.cpp \
	SoundTouch/SoundTouch.cpp \
	SoundTouch/sse_optimized.cpp \
	SoundTouch/TDStretch.cpp \
	soundtouch_wapper.cpp \

include $(BUILD_SHARED_LIBRARY)
