/*
 * Copyright (c) 2014-2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('ClinicalDefaults', {
    allQueries: {
        performedby: {
            defaultValue: LABKEY.Security.currentUser.displayName
        },
    },
    byQuery: {
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        },
        'study.procedure': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        },
        'study.clinremarks': {
            category: {
                defaultValue: 'Clinical',
                hidden: true,
                allowBlank: false
            }
        },
        'study.blood': {
            reason: {
                defaultValue: 'Clinical'
            }
        },
        'study.cases': {
            qcstate: {
                hidden: true
            },
            openDiagnosis: {
                hidden: true
            },
            closeDiagnosis: {
                hidden: true
            },
            attachmentFile: {
                hidden: true
            },
            category: {
                hidden: true
            },
            caseCategory: {
                hidden: true
            }
        }
    }
});