function hormone(suffix,result1,result2,result3,result4,result5,result6)
    horms = {}
    sensed = false
    for k = 1,7,1 do
        horms[k] = 0
    end

    if(suffix==robotCount-2) then
        if (result1==1) then 
            horms[2] = 1
            sensed = true
        end
    end
    if(suffix==-1) then
        if (result6==1) then 
            horms[7] = 1
            sensed = true
        end
    end
    if(result2==1) then
        horms[3] = 1
        sensed = true
    end
    if(result3==1) then
        horms[4] = 1
        sensed = true
    end
    if(result4==1) then
        horms[5] = 1
        sensed = true
    end
    if(result5==1) then
        horms[6] = 1
        sensed = true
    end 
    if not sensed then 
        horms[1] = 1
    end

    return horms               
end


function receptors(horm)
    for k = 1,7,1 do
        if(horm[k] == 1) then
            --if(suffix==3) then
                --    print('Went to 0')
            --    end
            if(amp<ampset[k]) then   --Amplitude
                amp = amp + ampstep[k]
            else
                amp = amp - ampstep[k]
            end    
            if(offset<offsetset[k]) then  --Offset
                offset = offset + offsetstep[k]
            else
                offset = offset - offsetstep[k]
            end    
            if(pfactor<pfset[k]) then --Phase
                pfactor = pfactor + pfstep[k]
            else
                pfactor = pfactor - pfstep[k]
            end    
            phasediff1 = math.pi*pfactor
            phasediff0 = math.pi*pfactor        
        end
    end
end

function propagate()
    test = math.random()
    if(test>0.25) then --Threshold 0.25
        return true
    else
        return false
    end
end


