/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('ClinicalDefaults', {
    // allQueries: {
    //     performedby: {
    //         defaultValue: LABKEY.Security.currentUser.displayName
    //     },
    // },
    byQuery: {
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: false,
                allowBlank: false,
                defaultValue: null
            }
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: false,
                allowBlank: false,
                defaultValue: null
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
            openRemark: {
                height: 120
            },
            plan: {
              height: 120
            },
            closeRemark: {
                height: 120
            },
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
        },
        'study.clinremarks': {
            hx: {
                formEditorConfig: {
                    xtype: 'ehr-hxtextarea'
                },
            },
            p:{
                height: 120
            },
            remark: {
                label: 'Remark',
                height: 120
            },
            vetreview : {
                height: 120
            },
            category: {
                getInitialValue: function (v, rec){
                    return 'Clinical'
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
            },
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.displayName
            },
            enddate: {
                hidden: true
            },
            dateFinalized: {
                hidden: true
            },
            qcstate: {
                hidden: true
            },
        }
    }
});