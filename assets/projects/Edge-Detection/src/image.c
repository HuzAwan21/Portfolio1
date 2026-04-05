#include "image.h"

#include <assert.h>
#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <float.h>

void apply_threshold(float *img, int w, int h, int T) {
    (void)img;
    (void)w;
    (void)h;
    (void)T;
    
    for (int i = 0; i < w * h; i++) {
        if (img[i]>T){
            img[i]=255;
        } else img[i]=0;
    }
}

void scale_image(float *result, const float *img, int w, int h) {
    (void)result;
    (void)img;
    (void)w;
    (void)h;

   int min = img[0];
   int max = img[0];

   for (int i =0; i<w*h; ++i){
    min = (img[i]<min)? img[i]: min;
   }
   for (int i =0; i<w*h; ++i){
    max = (img[i]>max)? img[i]: max;
   }

   for (int i=0; i<w*h; ++i){
     result[i]=(max==min)? 0:(((img[i]-min)/(max - min))*255);
   }

}

float get_pixel_value(const float *img, int w, int h, int x, int y) {
    (void)img;
    (void)w;
    (void)h;
    (void)x;
    (void)y;

    //Mirror x coordinate

    x= (x<0)? (-1*(x+1)):((x>=w)? (2*w -x -1): x);
    
    //Mirror y coordinate
    
    y= (y<0)? (-1*(y+1)):((y>=h)? (2*h -y -1): y);

    //Return the pixel value within the valid range
    return img[y * w + x];

}

float *array_init(int size) {
    (void) size;
    
   return (float*) malloc(size * sizeof(float));

}

void array_destroy(float *m) {
    (void)m;

     free(m);
}

float *read_image_from_file(const char *filename, int *w, int *h) {
    (void)filename;
    (void)w;
    (void)h;

    FILE *file = fopen(filename, "r");
    if (!file) {
        return NULL; //File does not exist
    }

    //Read the header
    char format[3];
    if (fscanf(file, "%2c", format) != 1 || strcmp(format, "P2") != 0) {
        fclose(file);
        return NULL; //Invalid file format
    }

    //Read image dimensions and maximum gray value
    int width, height, max_gray;
    if (fscanf(file, "%d %d", &width, &height) != 2 || 
        fscanf(file, "%d", &max_gray) != 1 || 
        width <= 0 || height <= 0 || max_gray != 255) {
        fclose(file);
        return NULL; //Invalid image dimensions or max gray value
    }

    //Allocate memory for the image
    float *img_data = array_init(width * height);
    if (!img_data) {
        fclose(file);
        return NULL; //Memory allocation failed
    }

    //Read pixel values
    int pixel_value;
    for (int i = 0; i < width * height; ++i) {
        if (fscanf(file, "%d", &pixel_value) != 1 || pixel_value < 0 || pixel_value > 255) {
            array_destroy(img_data);
            fclose(file);
            return NULL; //Invalid pixel value or not enough pixels
        }
        img_data[i] = (float)pixel_value;
    }

    // Check for extra pixels
    if (fscanf(file, "%d", &pixel_value) == 1) {
        array_destroy(img_data);
        fclose(file);
        return NULL; //Too many pixels
    }

    fclose(file);
    *w = width;
    *h = height;
    return img_data;

}

void write_image_to_file(const float *img, int w, int h, const char *filename) {
    (void)img;
    (void)w;
    (void)h;
    (void)filename;

    FILE *file = fopen(filename, "w");
    if (!file) {
        return; //Could not open file
    }

    //Write the header
    fprintf(file, "P2\n%d %d\n255\n", w, h);

    //Write pixel values
    for (int i = 0; i < w * h; ++i) {
        int pixel_value = (int)img[i];
        fprintf(file, "%d ", pixel_value);
    }

    fclose(file);
}
