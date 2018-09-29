#ifndef _BASE64_H_
#define _BASE64_H_

#include <stddef.h>

struct MyString{
    char * data;
    int length;
};

/**
 * 把这些标准的表更改一下不是更好！
 */
static const char base[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
char* base64_encode(const char*, int);
char* base64_decode(const char*, int);
static char find_pos(char);

#endif
