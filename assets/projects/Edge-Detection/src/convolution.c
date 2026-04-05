#include "convolution.h"

#include <stdlib.h>

#include "image.h"

void convolve(float *result, const float *img, int w, int h,
              const float *matrix, int w_m, int h_m) {
    (void)result;
    (void)img;
    (void)w;
    (void)h;
    (void)matrix;
    (void)w_m;
    (void)h_m;

     for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
            //Initialize the result for the current pixel
            float sum = 0.0;

            //Iterate over each element in the convolution matrix
            for (int i = 0; i < w_m; i++) {
                for (int j = 0; j < h_m; j++) {
                    //Compute the coordinates for the current element in the convolution matrix
                    int img_x = x - (w_m / 2) + i;
                    int img_y = y - (h_m / 2) + j;

                    //Get the pixel value using the mirror edge handling strategy
                    float pixel_value = get_pixel_value(img, w, h, img_x, img_y);

                    //Multiply the pixel value by the corresponding value in the convolution matrix
                    sum += pixel_value * matrix[j * w_m + i];
                }
            }

            //Store the result for the current pixel
            result[y * w + x] = sum;
        }
    }

}
