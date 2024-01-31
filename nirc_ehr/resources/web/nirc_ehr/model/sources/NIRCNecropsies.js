/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('NIRCNecropsies', {
    byQuery: {
        'study.necropsy': {
            // grossAbnormalities: {
            //     xtype: 'ehr-remarkfield',
            //     label: 'Gross Abnormalities',
            //     allowBlank: false,
            //     resizeDirections: 's',
            //     height: 200,
            //     columnConfig: {
            //         width: 200,
            //         height: 200
            //     },
            // },
            // diagnosis: {
            //     xtype: 'ehr-remarkfield',
            //     label: 'Diagnosis',
            //     allowBlank: false,
            //     resizeDirections: 's',
            //     height: 100,
            //     columnConfig: {
            //         width: 200,
            //         height: 200
            //     },
            // },
            // relevantHistory: {
            //     xtype: 'ehr-remarkfield',
            //     label: 'Relevant History',
            //     allowBlank: false,
            //     resizeDirections: 's',
            //     height: 200,
            //     columnConfig: {
            //         width: 200,
            //         height: 200
            //     },
            // }
        },
        'study.deaths': {
            performedBy: {
                label: 'Performed By'
            }
        },
        'study.grossPathology': {
            remark: {
                hidden: true,
                showInGrid: false
            },
            project: {
                hidden: true,
                showInGrid: false
            },
            systemExamined: {
                columnConfig: {
                    width: 200
                }
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
        },
        'study.tissueDisposition': {
            remark: {
                hidden: true,
                showInGrid: false
            },
            project: {
                hidden: true,
                showInGrid: false
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
            necropsyDispositionCode: {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            }
        }
    }
});