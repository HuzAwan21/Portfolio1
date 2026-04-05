#include <stdio.h>
#include <stdlib.h>

#include "argparser.h"
#include "convolution.h"
#include "derivation.h"
#include "gaussian_kernel.h"
#include "image.h"

int main(int const argc, char **const argv) {
    /**
     * Parse arguments. The parsed image file name and threshold are available
     * in the image_file_name and threshold global variables (see argparser.h).
     */

    parse_arguments(argc, argv);
    printf("Computing edges for image file %s with threshold %i\n", image_file_name, threshold);

    /**
     * Read Image from given file.
     *
     * If the input file is broken terminate with return value 1.
     *
     * Hint: The width and height of the image have to be accessible in the
     * scope of this function.
     */


    int w, h;
    float* img = read_image_from_file(image_file_name, &w, &h);
    if (!img) {
        fprintf(stderr, "Failed to read image from file %s\n", image_file_name);
        return 1;
    }

    /**
     * Blur the image by using convolve with the given Gaussian kernel matrix
     * gaussian_k (defined in gaussian_kernel.h). The width of the matrix is
     * gaussian_w, the height is gaussian_h.
     *
     * Afterwards, write the resulting blurred image to the file out_blur.pgm.
     */

    float* blurred_img = array_init(w * h);
    if (!blurred_img) {
        fprintf(stderr, "Failed to allocate memory for blurred image\n");
        array_destroy(img);
        return 1;
    }
    convolve(blurred_img, img, w, h, gaussian_k, gaussian_w, gaussian_h);
    write_image_to_file(blurred_img, w, h, "out_blur.pgm");

    /**
     * Compute the derivation of the blurred image computed above in both x and
     * y direction.
     *
     * Afterwards, rescale both results and write them to the files out_d_x.pgm
     * and out_d_y.pgm respectively.
     */

    float* der_x = array_init(w * h);
    float* der_y = array_init(w * h);

    if (!der_x || !der_y) {
        fprintf(stderr, "Failed to allocate memory for image derivations\n");
        array_destroy(img);
        array_destroy(blurred_img);
        return 1;
    }

    derivation_x_direction(der_x, blurred_img, w, h);
    derivation_y_direction(der_y, blurred_img, w, h);

    float *der_x_rescaled = array_init(w * h);
    float *der_y_rescaled = array_init(w * h);
    if (!der_x_rescaled || !der_y_rescaled) {
        fprintf(stderr, "Failed to allocate memory for rescaled derivations\n");
        array_destroy(img);
        array_destroy(blurred_img);
        array_destroy(der_x);
        array_destroy(der_y);
        return 1;
    }

    scale_image(der_x_rescaled, der_x, w, h);
    scale_image(der_y_rescaled, der_y, w, h);
    write_image_to_file(der_x_rescaled, w, h, "out_d_x.pgm");
    write_image_to_file(der_y_rescaled, w, h, "out_d_y.pgm");


    /**
     * Compute the gradient magnitude of the blurred image by using the
     * (unscaled!) derivations in x- and y-direction computed earlier.
     *
     * Afterwards, rescale the result and write it to out_gm.pgm.
     */

    float* grad_mag_img = array_init(w * h);

    if (!grad_mag_img) {
        fprintf(stderr, "Failed to allocate memory for gradient magnitude image\n");
        array_destroy(img);
        array_destroy(blurred_img);
        array_destroy(der_x);
        array_destroy(der_y);
        array_destroy(der_x_rescaled);
        array_destroy(der_y_rescaled);
        return 1;
    }

    gradient_magnitude(grad_mag_img, der_x, der_y, w, h);
    
    float* grad_mag_rescaled = array_init(w * h);
    if (!grad_mag_rescaled) {
        fprintf(stderr, "Failed to allocate memory for rescaled gradient magnitude\n");
        array_destroy(img);
        array_destroy(blurred_img);
        array_destroy(der_x);
        array_destroy(der_y);
        array_destroy(der_x_rescaled);
        array_destroy(der_y_rescaled);
        array_destroy(grad_mag_img);
        return 1;
    }

    scale_image(grad_mag_rescaled, grad_mag_img, w, h);
    write_image_to_file(grad_mag_rescaled, w, h, "out_gm.pgm");
    
    /**
     * Apply the threshold to the gradient magnitude.
     * Then write the result to the file out_edges.pgm.
     */

    apply_threshold (grad_mag_img, w, h, threshold);
    write_image_to_file(grad_mag_img, w, h, "out_edges.pgm");

    /**
     * Remember to free dynamically allocated memory when it is no longer used!
     */

    array_destroy(img);
    array_destroy(blurred_img);
    array_destroy(der_x);
    array_destroy(der_y);
    array_destroy(der_x_rescaled);
    array_destroy(der_y_rescaled);
    array_destroy(grad_mag_img);
    array_destroy(grad_mag_rescaled);

    return 0;
}
