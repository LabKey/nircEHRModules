EHR.model.DataModelManager.registerMetadata('Default', {
    allQueries: {
        Id: {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/dam': {
            xtype: 'ehr-animalIdUpperField',
        },
        'Id/demographics/sire': {
            xtype: 'ehr-animalIdUpperField',
        },
        performedby: {
            hidden: false,
            defaultValue: LABKEY.Security.currentUser.id.toString(),
            getInitialValue: function (v, rec) {
                return LABKEY.Security.currentUser.id;
            },
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
        },
        orderedby: {
            hidden: false,
            allowBlank: false,
            defaultValue: null,
            columnConfig: {
                width: 160
            }
        },
        scheduleddate: {
            header: 'Scheduled Date/Time',
            label: 'Scheduled Date/Time',
            hidden: false,
            userEditable: false,
            nullable: true,
            columnConfig: {
                width: 180
            }
        },
        date: {
            columnConfig: {
                fixed: true,
                width: 150
            }
        },
        enddate: {
            columnConfig: {
                fixed: true,
                width: 150
            }
        },
        'cage': {
            columnConfig: {
                fixed: true,
                width: 150
            },
        },
    },
    byQuery: {
        'study.housing': {
            room: {
                allowBlank: true,
                hidden: true
            },
            'cage': {
                allowBlank: false
            },
            'enddate': {
                hidden: true
            },
            performedby: {
                allowBlank: false,
                lookup: {
                    schemaName: 'core',
                    queryName: 'users',
                    keyColumn: 'UserId',
                    displayColumn: 'DisplayName',
                },
            },
            reason: {
                defaultValue: null,
                allowBlank: false,
                columnConfig: {
                    width: 180
                },
                lookup: {
                    filterArray: [LABKEY.Filter.create('date_disabled', null, LABKEY.Filter.Types.ISBLANK)]
                }
            },
        },
        'study.arrival': {
            initialRoom: {
                allowBlank: true,
                hidden: true
            }
        },
        'study.birth': {
            room: {
                allowBlank: true,
                hidden: true
            }
        },
        'study.exemptions': {
            category: {
                columnConfig: {
                    width: 300
                }
            }
        },
        'study.notes': {
            remark: {
                hidden: false,
                columnConfig: {
                    width: 400
                }
            }
        },
        'study.treatment_order': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            endTreatmentOrderedBy: {
                columnConfig: {
                    width: 200
                }
            }
        },
        'study.drug': {
            category: {
                defaultValue: 'Clinical',
                hidden: true
            },
            treatmentid: {
                hidden: true,
                nullable: true
            }
        },
        'study.observation_order': {
            category: {
                allowBlank: false,
                editorConfig: {
                    plugins: [Ext4.create('LDK.plugin.UserEditableCombo', {
                        allowChooseOther: false
                    })]
                },
                lookup: {
                    columns: 'value,description'
                },
                columnConfig: {
                    width: 200
                }
            },
            area: {
                defaultValue: 'N/A',
                columnConfig: {
                    width: 200
                }
            },
            observation: {
                columnConfig: {
                    width: 200
                }
            },
            remark: {
                columnConfig: {
                    width: 300
                }
            },
            frequency: {
                columnConfig: {
                    width: 180
                },
                nullable: false,
                allowBlank: true
            }
        },
        'study.pairings': {
            date : {
                getInitialValue: function(v, rec){
                    if (v)
                        return v;

                    let curDate = new Date();
                    curDate.setHours(0, 0, 0, 0);
                    return curDate;
                }
            },
            type: {
                hidden: true
            },
            pairid: {
                hidden: false,
                columnConfig: {
                    width: 150
                },
            },
            formationType: {
                columnConfig: {
                    width: 150
                }
            },
            reason: {
                columnConfig: {
                    width: 150
                },
            },
            goal: {
                columnConfig: {
                    width: 150
                },
            },
            endState: {
                columnConfig: {
                    width: 150
                },
            },
            remark: {
                title: 'Remark on Formation',
                columnConfig: {
                    width: 200
                }
            },
            separationRemark: {
                columnConfig: {
                    width: 200
                }
            }
        }
    }
});