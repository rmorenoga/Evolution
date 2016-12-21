function updateCPG(teta,ampli,dampli,offset,doffset,dt,ampd,offd,phasediff,v,wij)

    local tetanew = teta[1] + dteta(teta,phasediff,v,wij) * dt;
    local amplinew = ampli + dampli * dt;
    local damplinew = dampli + ddampli(dampli,ampli,ampd) * dt;
    local offsetnew = offset + doffset * dt;
    local doffsetnew = doffset + ddoffset(doffset,offset,offd) * dt;    
        
    return tetanew,amplinew,damplinew,offsetnew,doffsetnew
end

function ddampli(dampli, ampli, ampd)
    ar = 50
    return ar*((ar/4)*(ampd-ampli)-dampli)
end
        
function ddoffset(doffset, offset, offd)
    ax = 30
    return ax*((ax/4)*(offd-offset)-doffset)
end

function dteta(teta,phasediff,v,wij)
    local comp = {} 
    for i=2,#teta do
        if (teta[i]~=-1) then
            comp[i-1] = (wij[i-1]*math.sin(teta[i]-teta[1] - (phasediff[i-1])))
            --comp[i-1] = (wij[i-1]*math.sin(teta[i]-teta[1] + ((phasediff[i-1]) * (-1^(i-1))) )) 
            --[[if (i-1==1) then
                comp[i-1] = (wij[i-1]*math.sin(teta[i]-teta[1] + (phasediff[i-1])  )) 
            else
                comp[i-1] = (wij[i-1]*math.sin(teta[i]-teta[1] - (phasediff[i-1])  ))
            end ]]
        else
            comp[i-1] = 0
        end
    end  

    local sum = 2*math.pi*v
    for i=1,#comp do
        sum = sum + comp[i]
    end
    return sum
end