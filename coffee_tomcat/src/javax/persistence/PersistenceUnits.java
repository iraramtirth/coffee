/*******************************************************************************
 * Copyright (c) 2008, 2009 Sun Microsystems. All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors:
 *     Linda DeMichiel - Java Persistence 2.0 - Version 2.0 (October 1, 2009)
 *     Specification available from http://jcp.org/en/jsr/detail?id=317
 *
 ******************************************************************************/
package javax.persistence;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;

import javax.persistence.PersistenceUnit;

import static java.lang.annotation.RetentionPolicy.*;


/**
 * Declares one or more {@link PersistenceUnit} annotations.
 *
 * @since Java Persistence 1.0
 */

@Target({TYPE})
@Retention(RUNTIME)
public @interface PersistenceUnits {

    /** (Required) One or more {@link PersistenceUnit} annotations. */
    PersistenceUnit[] value();

}
