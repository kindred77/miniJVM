cmake_minimum_required(VERSION 3.8)
project(jni_sdl2 C CXX)


set(CMAKE_CXX_STANDARD 11)
set(CMAKE_C_STANDARD 99)

include_directories(imgui/ kiss/ imgui/backends/ ../../../minijvm/c/jvm)

set(SOURCE_FILES
        media.h
        jni_sdl2.c
        jni_mir.c
        media.c

        # imgui/imconfig.h
        # imgui/imgui.h
        # imgui/imgui_internal.h
        # imgui/imstb_rectpack.h
        # imgui/imstb_textedit.h
        # imgui/imstb_truetype.h
        # imgui/backends/imgui_impl_sdl2.h

        imgui/imgui_draw.cpp
        imgui/imgui_tables.cpp
        imgui/imgui_widgets.cpp
        imgui/imgui.cpp
        imgui/backends/imgui_impl_sdl2.cpp
        imgui/backends/imgui_impl_sdlrenderer2.cpp

        kiss/kiss_draw.c
        kiss/kiss_general.c
        kiss/kiss_posix.c
        kiss/kiss_widgets.c

        jni_kiss.c

        mir_imgui.cpp
        
        )
#add_executable(test_sdl2 ${SOURCE_FILES})

# add_executable(test_kiss 
#     kiss/kiss_draw.c
#     kiss/kiss_general.c
#     kiss/kiss_posix.c
#     kiss/kiss_widgets.c
#     kiss/kiss_example1.c
# )

IF (APPLE)
    MESSAGE(FATAL "Not supported platform.")
ELSEIF (UNIX)
    include_directories(depends/ubuntu_x64/include/ depends/ubuntu_x64/include/SDL2/)
ELSEIF (MINGW)
    include_directories(depends/win_x64/include/ depends/win_x64/include/SDL2/)
ENDIF ()

add_library(jni_mir SHARED ${SOURCE_FILES})


#find_package(OpenGL REQUIRED)
MESSAGE(STATUS "CMAKE_SYSTEM :${CMAKE_SYSTEM}")
MESSAGE(STATUS "CMAKE_SYSTEM_NAME :${CMAKE_SYSTEM_NAME}")
MESSAGE(STATUS "CMAKE_SYSTEM_PROCESSOR :${CMAKE_SYSTEM_PROCESSOR}")
MESSAGE(STATUS "WIN32 :${WIN32}")
MESSAGE(STATUS "CMAKE_GENERATOR :${CMAKE_GENERATOR}")
MESSAGE(STATUS "CMAKE_SIZEOF_VOID_P  :${CMAKE_SIZEOF_VOID_P}")
MESSAGE(STATUS "CMAKE_SOURCE_DIR  :${CMAKE_SOURCE_DIR}")


IF (APPLE)
    MESSAGE(FATAL "Not supported platform.")
    find_library(SDL2_LIB libsdl2.a depends/lib/mac_x64/)
    MARK_AS_ADVANCED(SDL2_LIB)
    SET(EXTRA_LIBS ${SDL2_LIB})

    TARGET_LINK_LIBRARIES(jni_mir ${EXTRA_LIBS})
    TARGET_LINK_LIBRARIES(test_sdl2 ${EXTRA_LIBS})
    SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/mac_x64)
ELSEIF (UNIX)
    MESSAGE("linux os here.")
    find_library(SDL2_LIB libSDL2.so PATHS "${CMAKE_SOURCE_DIR}/depends/ubuntu_x64/lib/" NO_DEFAULT_PATH)
    find_library(SDL2_TTF_LIB libSDL2_ttf.so PATHS "${CMAKE_SOURCE_DIR}/depends/ubuntu_x64/lib/" NO_DEFAULT_PATH)
    MARK_AS_ADVANCED(SDL2_LIB)
    MESSAGE("${SDL2_LIB}----------${SDL2_TTF_LIB}")
    TARGET_LINK_LIBRARIES(jni_mir PUBLIC ${SDL2_LIB} PUBLIC ${SDL2_TTF_LIB})
    TARGET_LINK_LIBRARIES(jni_mir
        PRIVATE
        pthread
        #gl
        PRIVATE -static-libstdc++
        )

    SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/ubuntu_x64)
    SET(DEPLIBS_SOURCE_PATH ${CMAKE_SOURCE_DIR}/depends/ubuntu_x64/lib)
    #copy SDL2 to target
    set(OUTPUT_DIR ${PROJECT_SOURCE_DIR}/../../../binary/ubuntu_x64)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/libSDL2.so
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/libSDL2.so
            ${OUTPUT_DIR}/libSDL2-2.0.so.0
        DEPENDS jni_mir
        COMMENT "Copying file libSDL2.so to output directory"
    )
    add_custom_target(copy_sdl_dll ALL DEPENDS ${OUTPUT_DIR}/libSDL2.so)

    #add_custom_command(
    #    OUTPUT ${OUTPUT_DIR}/SDL2_image.dll
    #    COMMAND ${CMAKE_COMMAND} -E copy_if_different
    #        ${DEPLIBS_SOURCE_PATH}/SDL2_image.dll
    #        ${OUTPUT_DIR}/SDL2_image.dll
    #    DEPENDS jni_mir
    #    COMMENT "Copying file SDL2_image.dll to output directory"
    #)
    #add_custom_target(copy_sdl_img_dlls ALL DEPENDS ${OUTPUT_DIR}/SDL2_image.dll)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/libSDL2_ttf.so
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/libSDL2_ttf.so
            ${OUTPUT_DIR}/libSDL2_ttf-2.0.so.0
        DEPENDS jni_mir
        COMMENT "Copying file libSDL2_ttf.so to output directory"
    )
    add_custom_target(copy_sdl_ttf_dlls ALL DEPENDS ${OUTPUT_DIR}/libSDL2_ttf.so)

ELSEIF (MINGW)
    IF (CMAKE_SIZEOF_VOID_P EQUAL 4)
        message("32bit")
        find_library(SDL2_LIB libsdl2.a "${CMAKE_SOURCE_DIR}/depends/win_i686/lib/")
        SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/win32)
        SET(DEPLIBS_SOURCE_PATH ${CMAKE_SOURCE_DIR}/depends/win_i686/lib)
    elseif (CMAKE_SIZEOF_VOID_P EQUAL 8)
        message("64bit")
        find_library(SDL2_LIB SDL2.dll "${CMAKE_SOURCE_DIR}/depends/win_x64/lib/")
        find_library(SDL2_IMG_LIB SDL2_image.dll "${CMAKE_SOURCE_DIR}/depends/win_x64/lib/")
        find_library(SDL2_TTF_LIB SDL2_ttf.dll "${CMAKE_SOURCE_DIR}/depends/win_x64/lib/")
        SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/../../../binary/win_x64)
        SET(DEPLIBS_SOURCE_PATH ${CMAKE_SOURCE_DIR}/depends/win_x64/lib)
    endif ()
    message(${SDL2_LIB})
    MARK_AS_ADVANCED(SDL2_LIB)
    TARGET_LINK_LIBRARIES(jni_mir PUBLIC ${SDL2_LIB} PUBLIC ${SDL2_IMG_LIB} PUBLIC ${SDL2_TTF_LIB}
                PRIVATE winmm
                PRIVATE imm32
                PRIVATE setupapi
                PRIVATE version
                PRIVATE opengl32)
    
    # TARGET_LINK_LIBRARIES(test_kiss PUBLIC ${SDL2_LIB} PUBLIC ${SDL2_IMG_LIB} PUBLIC ${SDL2_TTF_LIB}
    #             PRIVATE winmm
    #             PRIVATE imm32
    #             PRIVATE setupapi
    #             PRIVATE version
    #             PRIVATE opengl32)    

    #all: TARGET_LINK_LIBRARIES(jni_mir kernel32 user32 gdi32 winmm imm32 ole32 oleaut32 version uuid advapi32 setupapi shell32)

    #TARGET_LINK_LIBRARIES(test_sdl2 winmm imm32)
    #TARGET_LINK_LIBRARIES(test_sdl2 ${SDL2_LIB})
    set(CMAKE_EXE_LINKER_FLAGS "-mwindows")

    # 启用 AddressSanitizer
    # set (SAN_FLAGS "${SAN_FLAGS} -g -fno-omit-frame-pointer -DSANITIZER")
    # set (ASAN_FLAGS "-fsanitize=address -fsanitize-address-use-after-scope")
    # set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${SAN_FLAGS} ${ASAN_FLAGS}")
    # set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${SAN_FLAGS} ${ASAN_FLAGS}")
    # if (CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    #     message("Enable Address Sanitize.")
    #     set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${ASAN_FLAGS}")
    # endif()
    # if (MAKE_STATIC_LIBRARIES AND CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    #     message("Using static.")
    #     set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -static-libasan")
    # endif ()
    # if (CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    #     message("Link asan staticly.")
    #     target_link_libraries(jni_mir INTERFACE "-Wl,-static -lasan -Wl,-Bdynamic")
    # endif()

    #copy SDL2 and SDL2_image dll to target
    set(OUTPUT_DIR ${PROJECT_SOURCE_DIR}/../../../binary/win_x64)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/SDL2.dll
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/SDL2.dll
            ${OUTPUT_DIR}/SDL2.dll
        DEPENDS jni_mir
        COMMENT "Copying file SDL2.dll to output directory"
    )
    add_custom_target(copy_sdl_dll ALL DEPENDS ${OUTPUT_DIR}/SDL2.dll)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/SDL2_image.dll
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/SDL2_image.dll
            ${OUTPUT_DIR}/SDL2_image.dll
        DEPENDS jni_mir
        COMMENT "Copying file SDL2_image.dll to output directory"
    )
    add_custom_target(copy_sdl_img_dlls ALL DEPENDS ${OUTPUT_DIR}/SDL2_image.dll)

    add_custom_command(
        OUTPUT ${OUTPUT_DIR}/SDL2_ttf.dll
        COMMAND ${CMAKE_COMMAND} -E copy_if_different
            ${DEPLIBS_SOURCE_PATH}/SDL2_ttf.dll
            ${OUTPUT_DIR}/SDL2_ttf.dll
        DEPENDS jni_mir
        COMMENT "Copying file SDL2_ttf.dll to output directory"
    )
    add_custom_target(copy_sdl_ttf_dlls ALL DEPENDS ${OUTPUT_DIR}/SDL2_ttf.dll)

ENDIF ()

message(${LIBRARY_OUTPUT_PATH})


