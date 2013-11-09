/*===========================================================================
COPYRIGHT 2013 Vinícius G. Mendonça ALL RIGHTS RESERVED.

This software cannot be copied, stored, distributed without
Vinícius G. Mendonça prior authorization.

This file was made available on https://github.com/ViniGodoy and it
is free to be redistributed or used under Creative Commons license 2.5 br:
http://creativecommons.org/licenses/by-sa/2.5/br/
============================================================================*/

package br.com.vinigodoy.raytracer.utility;

public class RaytraceException extends IllegalArgumentException {
    public RaytraceException(String message, Object... params) {
        super(String.format(message, params));
    }

    public RaytraceException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }

    public RaytraceException(Throwable cause) {
        this(cause, "A problem occured while ray tracing!");
    }
}
