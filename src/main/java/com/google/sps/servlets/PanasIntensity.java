package com.google.sps.servlets;

import java.util.HashMap;
import java.util.Map;

/**
 * Feeling intensities on the PANAS emotional survey.
 *
 * https://www.brandeis.edu/roybal/docs/PANAS-GEN_website_PDF.pdf
 */
public enum PanasIntensity {
    NOT_AT_ALL,
    VERY_SLIGHTLY,
    A_LITTLE,
    MODERATELY,
    QUITE_A_BIT,
    EXTREMELY;

    public static final PanasIntensity values[] = values();
}
