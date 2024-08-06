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
                hidden: false,
                allowBlank: false,
                defaultValue: null,
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'veterinarians',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName'
                }
            },
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            performedby: {
                hidden: false,
                allowBlank: false,
                defaultValue: null,
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'veterinarians',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName'
                }
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
            },
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.id.toString(),
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',

                        // 'performedby' is a text field in the dataset and its lookup to the userid is an int field - this mismatch causes it to disappear
                        // from the display when a value is selected from the dropdown even though the 'userid' value gets saved as a text (this behavior was only seen
                        // in the form panel but not in the grid panel).
                        // casting it as a varchar when loading the store fixes this issue.
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                }
            }
        },
        'study.blood': {
            reason: {
                defaultValue: 'Clinical'
            },
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.id.toString(),
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',

                        // 'performedby' is a text field in the dataset and its lookup to the userid is an int field - this mismatch causes it to disappear
                        // from the display when a value is selected from the dropdown even though the 'userid' value gets saved as a text (this behavior was only seen
                        // in the form panel but not in the grid panel).
                        // casting it as a varchar when loading the store fixes this issue.
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                }
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
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.id.toString(),
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                }
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
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.displayName,
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
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
        'study.clinical_observations': {
            performedby: {
                hidden: false,
                defaultValue: LABKEY.Security.currentUser.id.toString(),
                editorConfig: {
                    store: {
                        type: 'labkey-store',
                        schemaName: 'core',
                        sql: 'SELECT CAST (UserId AS VARCHAR) AS UserId,DisplayName,FirstName,LastName FROM core.users',
                        autoLoad: true
                    }
                }
            }
        }
    }
});