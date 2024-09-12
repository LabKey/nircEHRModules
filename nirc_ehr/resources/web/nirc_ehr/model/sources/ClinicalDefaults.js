/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
EHR.model.DataModelManager.registerMetadata('ClinicalDefaults', {
    byQuery: {
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: true
            }
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            scheduledDate: {
                columnConfig: {
                    width: 130,
                    fixed: true
                },
            },
            treatmentId: {
                hidden: true
            }
        },
        'study.prc': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            // procedure data is not categorized, so not using procedure_category based selection
            procedure: {
                columnConfig: {
                    width: 250
                }
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
                defaultValue: 'Clinical',
                hidden: true
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
            },
            formCasesLink: {
                xtype: 'nirc_ehr-editCases'
            }
        },
        'study.clinremarks': {
            hx: {
                formEditorConfig: {
                    xtype: 'ehr-hxtextarea'
                },
            },
            p: {
                height: 120
            },
            remark: {
                label: 'Remark',
                height: 120
            },
            vetreview: {
                height: 120
            },
            category: {
                getInitialValue: function (v, rec) {
                    return 'Clinical'
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
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
        },
        'study.vitals': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            }
        }
    }
});